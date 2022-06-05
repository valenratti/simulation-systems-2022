import cell_index_method.CIMConfig;
import cell_index_method.CellIndexMethod;
import granular_media.ForceException;
import granular_media.GranularMedia;
import model.Particle;
import model.Wall;
import utils.Beeman;
import utils.FileWriter;
import utils.Utils;
import utils.Vector;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Simulator {

    public static void simulate(final CIMConfig config, final double dt, final double dt2, final double kn, final double kt) throws IOException {
        CellIndexMethod cellIndexMethod = new CellIndexMethod(config);
        double boxWidth = config.getAreaWidth(), boxHeight = config.getAreaHeight();
        int caudal = 0;
        double slidingWindow = 0.0;
//        int slidingWindowBySteps = (int) Math.floor(slidingWindow / dt);
//        int dtCaudalInteger = slidingWindowBySteps / 10;
//        List<Integer> caudalsBySlidingWindow = new ArrayList<>();
        List<Vector> caudalList = new ArrayList<>();
        List<Double> caudalTimes = new ArrayList<>();
        Beeman beeman = new Beeman(dt, true, new GranularMedia(kn, kt, boxWidth, boxHeight));

        List<Particle> particleList = cellIndexMethod.getArea().getParticleList();

        AtomicBoolean flowStabilized = new AtomicBoolean(false);

        List<Vector> energies = new ArrayList<>(); // time unit --> seconds

        double time = 0.0;
        int printAux = 0;
        int slidingWindowAux = 0;
        final double logStep = dt2 / dt;    // dt2 = logStep * dt

        // TODO: log initial state
        FileWriter.printPositions(config.getExitWidth(), particleList);
        boolean error = false;
        while(time<=5.0 && !error) {
            if(time != 0.0) {
                cellIndexMethod.updateParticles(particleList);
            }
            time += dt;
            Map<Particle, List<Particle>> neighboursMap = cellIndexMethod.calculateNeighbours();

            for(Particle particle : particleList.stream().filter(particle -> !particle.isFixed()).collect(Collectors.toList())){
                try {
                    beeman.nextStep(particle, neighboursMap.getOrDefault(particle, new ArrayList<>()),
                            getWallsCollisions(particle, boxWidth, boxHeight, config.getExitWidth()));
                } catch (ForceException e) {
                    error = true;
                    break;
                }
                if(particle.getY() <= -config.getHeightBelowExit()) {
                    // TODO: reset particles L/10 below exit
                    // TODO: caudal = nro de particulas que salieron en dt / dt
                    if(++caudal == 50){
                        slidingWindow = time;
                    }
                    if(caudal >= 50){
                        if(caudal == 50){
                            slidingWindow = time;
                        }else {
                            slidingWindow = time - caudalTimes.get(0);
                        }
                        caudalList.add(new Vector(time, 50 / slidingWindow));
                        caudalTimes.remove(0);
                        if(caudal > 50) {
                            if (Math.abs(caudalList.get(caudalList.size() - 1).getY() - caudalList.get(caudalList.size() - 2).getY()) <= 0.5) {
                                flowStabilized.set(true);
                            }
                        }
                    }
                    caudalTimes.add(time);
                    resetParticle(particle, config, particleList);
                }
            }


            energies.add(new Vector(time, Utils.calculateSystemKineticEnergy(particleList)));

            // TODO: update flowStabilized if appropriate


            printAux++;
            if(printAux == 500) {
                FileWriter.printPositions(config.getExitWidth(), particleList);
                printAux = 0;
            }

//            slidingWindowAux++;
//            if(slidingWindowAux % dtCaudalInteger == 0){
//                caudalsBySlidingWindow.add(caudal.get());
//                caudal.set(0);
//            }
//            if(slidingWindowAux >= slidingWindowBySteps && slidingWindowAux % dtCaudalInteger == 0){
//                int totalCaudal = caudalsBySlidingWindow.subList(caudalsBySlidingWindow.size() - (slidingWindowBySteps / dtCaudalInteger),
//                        caudalsBySlidingWindow.size()).stream().mapToInt(Integer::intValue).sum();
//
//                caudalList.add((double) totalCaudal / slidingWindow);
//            }


            cellIndexMethod.clear();
        }
        FileWriter.reset();
        // TODO: Beverloo

        // TODO: valor medio y desv estandar del caudal
        // TODO: print time vs flow
        // TODO: print time vs kinetic energy
        FileWriter.printCaudal(config.getExitWidth(),caudalList, slidingWindow);
        FileWriter.printEnergies(config.getExitWidth(), energies, kt);
    }

    private static void resetParticle(Particle particle, CIMConfig config, List<Particle> particles) {
        double x;
        double y;
        do{
            x = ThreadLocalRandom.current().nextDouble(config.getMaxParticleRadius(),config.getAreaWidth()-config.getMaxParticleRadius());
            y = ThreadLocalRandom.current().nextDouble(config.getAreaHeight() / 3 + config.getMaxParticleRadius(), config.getAreaHeight() - config.getMaxParticleRadius());
        }while (overlapParticle(x,y,particle.getRadius(), particles));  //NO NEED TO CHECK WALLS?
        particle.setX(x);
        particle.setY(y);
        particle.setVx(0d);
        particle.setVy(0d);
        particle.setPressure(0d);
    }

    private static boolean overlapParticle(double x, double y, double radius, List<Particle> particles) {
        if (particles.size() == 0) return true;
        Particle aux = new Particle(x, y, 0d, 0d, 0d, radius, true);
        for (Particle particle : particles.stream().filter(particle -> !particle.isFixed()).collect(Collectors.toList())){
            if(aux.getOverlap(particle) > 0){
                return true;
            }
        }
        return false;
    }

    private static List<Wall> getWallsCollisions(Particle p, Double boxWidth, Double boxHeight, Double D){
        List<Wall> walls = new LinkedList<>();
        if (p.getX() < p.getRadius())
            walls.add(new Wall(Wall.typeOfWall.LEFT));
        if (boxWidth - p.getX() < p.getRadius())
            walls.add(new Wall(Wall.typeOfWall.RIGHT));
        if (p.getY() < p.getRadius() && p.getY()>0)
            if(p.getX() < boxWidth / 2 - D / 2  || p.getX() > boxWidth / 2 + D / 2 )
                walls.add(new Wall(Wall.typeOfWall.BOTTOM));
        return walls;
    }

}
