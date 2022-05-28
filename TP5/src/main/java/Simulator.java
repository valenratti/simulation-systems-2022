import cell_index_method.CIMConfig;
import cell_index_method.CellIndexMethod;
import granular_media.GranularMedia;
import model.Particle;
import utils.Beeman;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
            nextStep(neighboursMap, particleList, beeman);

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

    private static void nextStep(Map<Particle, List<Particle>> neighboursMap, List<Particle> particleList, Beeman beeman) {
        List<Particle> interactionParticles;

        for(Particle particle : neighboursMap.keySet()) {
            // TODO: add collisions with walls before calling beeman.nextStep
            beeman.nextStep(particle, neighboursMap.get(particle), Collections.emptyList());
        }
    }

}
