import cell_index_method.CIMConfig;
import cell_index_method.CellIndexMethod;
import granular_media.GranularMedia;
import model.Particle;
import model.Wall;
import utils.Beeman;
import utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Simulator {

    public static void simulate(final CIMConfig config, final double dt, final double dt2) {
        CellIndexMethod cellIndexMethod = new CellIndexMethod(config);
        double kn = 10e+5, kt = 2*kn, boxWidth = 0.3, boxHeight = 1.0;
        Beeman beeman = new Beeman(dt, true, new GranularMedia(kn, kt, boxWidth, boxHeight));

        List<Particle> particleList = cellIndexMethod.getArea().getParticleList();

        List<Double> flowList = new ArrayList<>(); // TODO: "sliding window"?
        boolean flowStabilized = false;

        List<Double> energies = new ArrayList<>(); // time unit --> seconds

        double time = 0.0;
        int aux = 0;    // aux = (time % dt2) / dt  <--> aux * dt = time + k * dt2
        final double logStep = dt2 / dt;    // dt2 = logStep * dt

        // TODO: log initial state

        while(!flowStabilized) {
            if(time != 0.0) {
                cellIndexMethod.updateParticles(particleList);
            }
            time += dt;
            Map<Particle, List<Particle>> neighboursMap = cellIndexMethod.calculateNeighbours();
            particleList.parallelStream().forEach((particle) -> {
                beeman.nextStep(particle, neighboursMap.getOrDefault(particle, new ArrayList<>()), getWallsCollisions(particle, boxWidth, boxHeight, config.getExitWidth()));
            });

            energies.add(Utils.calculateSystemKineticEnergy(particleList));

            // TODO: caudal = nro de particulas que salieron en dt / dt
            // TODO: update flowStabilized if appropriate
            // TODO: reset particles L/10 below exit

            aux++;

            if(aux == logStep) {
                // TODO: log
                aux = 0;
            }
        }

        // TODO: Beverloo

        // TODO: valor medio y desv estandar del caudal
        // TODO: print time vs flow
        // TODO: print time vs kinetic energy
        cellIndexMethod.clear();
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
