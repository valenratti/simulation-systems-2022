import cell_index_method.CIMConfig;
import cell_index_method.CellIndexMethod;
import model.Particle;
import utils.Beeman;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Simulator {

    public static void simulate(final CIMConfig config, final double dt, final double dt2) {
        CellIndexMethod cellIndexMethod = new CellIndexMethod(config);
        Beeman beeman = new Beeman(dt, true);

        List<Particle> particleList = cellIndexMethod.getArea().getParticleList();

        List<Double> flowList = new ArrayList<>(); // TODO: "sliding window"?
        boolean flowStabilized = false;

        List<Double> energies = new ArrayList<>(); // time unit --> seconds

        double time = 0.0;
        int aux = 0;    // aux = (time % dt2) / dt  <--> aux * dt = time + k * dt2
        final double logStep = dt2 / dt;    // dt2 = logStep * dt

        // TODO: log initial state

        while(!flowStabilized) {
            time += dt;

            Map<Long, List<Long>> neighboursMap = cellIndexMethod.calculateNeighbours();
            particleList.forEach(beeman::nextStep); // FIXME: con el neighboursMap, no con particleList

            // TODO: collisions with walls

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

    }

}
