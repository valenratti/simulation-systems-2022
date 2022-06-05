import cell_index_method.CIMConfig;
import cell_index_method.CellIndexMethod;
import granular_media.GranularMedia;
import model.Cell;
import model.CellCoordinates;
import model.Particle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Beeman;
import utils.FileWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GranularMediaTest {

    @Test
    public void afterResetingParticlesInCIM_ShouldBeInSameCell() throws IOException {
        final double L = 1.0;   // L en [1, 1.5] m
        final double W = 0.3;   // W en [0.3, 0.4] m
        final double D = 0.15;  // D en [0.15, 0.25] m
        final double minRadius = 0.01, maxRadius = 0.015;   // r en [0.01, 0.015] m
        final int N = 1000;  // number of particles TBD
        final double m = 0.01;  // kg
        final CIMConfig config = new CIMConfig(L, W, D, N, minRadius, maxRadius, m, L / 10);
        CellIndexMethod cellIndexMethod = new CellIndexMethod(config);
        Map<Cell, List<Particle>> beforeClearing = new HashMap<>();
        cellIndexMethod.getCellMap().values().forEach((cell) -> beforeClearing.put(cell, cell.getParticleList()));
        FileWriter.printPositions(D, cellIndexMethod.getArea().getParticleList());
        cellIndexMethod.clear();
        cellIndexMethod.updateParticles(cellIndexMethod.getArea().getParticleList());
        Map<Cell, List<Particle>> afterUpdating = new HashMap<>();
        cellIndexMethod.getCellMap().values().forEach((cell) -> afterUpdating.put(cell, cell.getParticleList()));
        for(Cell cell : afterUpdating.keySet()){
            if(afterUpdating.get(cell).size() > 0)
                Assertions.assertEquals(afterUpdating.get(cell).get(0), beforeClearing.get(cell).get(0));
        }
    }

    @Test
    void validateOneParticlePerCell(){
        final double L = 1.0;   // L en [1, 1.5] m
        final double W = 0.3;   // W en [0.3, 0.4] m
        final double D = 0.15;  // D en [0.15, 0.25] m
        final double minRadius = 0.01, maxRadius = 0.015;   // r en [0.01, 0.015] m
        final int N = 1000;  // number of particles TBD
        final double m = 0.01;  // kg
        final CIMConfig config = new CIMConfig(L, W, D, N, minRadius, maxRadius, m, L / 10);
        double dt = 1e-4, dt2 =1e-2;
        CellIndexMethod cellIndexMethod = new CellIndexMethod(config);
        for(Cell cell : cellIndexMethod.getCellMap().values().stream().filter((cell) -> cell.getRow() < 33).collect(Collectors.toList())){
            Assertions.assertEquals(1, cell.getParticleList().size());
        }
    }

    @Test
    void test() throws IOException {
        final double L = 1.0;   // L en [1, 1.5] m
        final double W = 0.3;   // W en [0.3, 0.4] m
        final double D = 0.15;  // D en [0.15, 0.25] m

        final double minRadius = 0.01, maxRadius = 0.015;   // r en [0.01, 0.015] m
        final int N = 150;  // number of particles TBD
        final double m = 0.01;  // kg

        final CIMConfig config = new CIMConfig(L, W, D, N, minRadius, maxRadius, m, L / 10);

        // dt de referencia --> dt = 0.1 * sqrt(m / kn)
        double dt = 5E-5/3, dt2 =1e-2;
//        dt = 0.1 * Math.sqrt(m / 1e5);

        Simulator.simulate(config, dt, dt2, 1e+5, 2e+5);
    }
}
