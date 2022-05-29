import cell_index_method.CIMConfig;
import cell_index_method.CellIndexMethod;
import granular_media.GranularMedia;
import model.Particle;
import model.Wall;
import utils.Beeman;
import utils.FileWriter;
import utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulator {

    public static void simulate(final CIMConfig config, final double dt, final double dt2) throws IOException {
        CellIndexMethod cellIndexMethod = new CellIndexMethod(config);
        double kn = 10e+5, kt = 2*kn, boxWidth = config.getAreaWidth(), boxHeight = config.getAreaHeight();
        Beeman beeman = new Beeman(dt, true, new GranularMedia(kn, kt, boxWidth, boxHeight));

        List<Particle> particleList = cellIndexMethod.getArea().getParticleList();

        List<Double> flowList = new ArrayList<>(); // TODO: "sliding window"?
        boolean flowStabilized = false;

        List<Double> energies = new ArrayList<>(); // time unit --> seconds

        double time = 0.0;
        AtomicInteger caudal = new AtomicInteger();
        int aux = 0;    // aux = (time % dt2) / dt  <--> aux * dt = time + k * dt2
        final double logStep = dt2 / dt;    // dt2 = logStep * dt

        // TODO: log initial state

        while(!flowStabilized) {
            if(time != 0.0) {
                cellIndexMethod.updateParticles(particleList);
            }
            time += dt;
            Map<Particle, List<Particle>> neighboursMap = cellIndexMethod.calculateNeighbours();
            particleList.forEach((particle) -> {
                beeman.nextStep(particle, neighboursMap.getOrDefault(particle, new ArrayList<>()),
                        getWallsCollisions(particle, boxWidth, boxHeight, config.getExitWidth()));
                if(particle.getY() >= boxHeight + config.getHeightBelowExit()){
                    // TODO: reset particles L/10 below exit
                    // TODO: caudal = nro de particulas que salieron en dt / dt
                    caudal.getAndIncrement();
                    resetParticle(particle, config, particleList);
                }
            });

            energies.add(Utils.calculateSystemKineticEnergy(particleList));

            // TODO: update flowStabilized if appropriate


            aux++;
            if(aux == 500) {
                FileWriter.printPositions(particleList);
                aux = 0;
            }
            cellIndexMethod.clear();
        }

        // TODO: Beverloo

        // TODO: valor medio y desv estandar del caudal
        // TODO: print time vs flow
        // TODO: print time vs kinetic energy
    }

    private static void resetParticle(Particle particle, CIMConfig config, List<Particle> particles) {
        double x;
        double y;
        do{
            x = ThreadLocalRandom.current().nextDouble(config.getMaxParticleRadius(),config.getAreaWidth()-config.getMaxParticleRadius());
            y = ThreadLocalRandom.current().nextDouble( 0, config.getAreaHeight()/3 + config.getMaxParticleRadius());
        }while (noOverlapParticle(x,y,particle.getRadius(), particles));  //NO NEED TO CHECK WALLS?
        particle.setX(x);
        particle.setY(y);
        particle.setVx(0d);
        particle.setVy(0d);
        particle.setPressure(0d);
    }

    private static boolean noOverlapParticle(double x, double y, double radius, List<Particle> particles) {
        if (particles.size() == 0) return true;
        for (Particle particle : particles){
            if ( (Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2)) <= Math.pow(particle.getRadius() + radius, 2)){
                return false;
            }
        }
        return true;
    }

    private static List<Wall> getWallsCollisions(Particle p, Double boxWidth, Double boxHeight, Double D){
        List<Wall> walls = new LinkedList<>();
        if (p.getX() < p.getRadius())
            walls.add(new Wall(Wall.typeOfWall.LEFT));
        if (boxWidth - p.getX() < p.getRadius())
            walls.add(new Wall(Wall.typeOfWall.RIGHT));
        if (boxHeight - p.getY() < p.getRadius())
            if(p.getX() < boxWidth / 2 - D / 2  || p.getX() > boxWidth / 2 + D / 2 )
                walls.add(new Wall(Wall.typeOfWall.BOTTOM));
        return walls;
    }

}
