import cell.Cell;
import grid.impl.Grid2D;
import grid.impl.InitializationGrid2D;
import org.junit.jupiter.api.Test;
import simulation.SimulationOptions;
import simulation.Simulator;
import simulation.State;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridTest {

    @Test
    public void whenInitializeGrid2D_AliveParticlesShouldMatchWithInitializationGrid(){
        State state = new State(1000, false);

        InitializationGrid2D initializationGrid2D = new InitializationGrid2D(500, 250000,100, 100);
        initializationGrid2D.initialize();

        Grid2D grid2D = new Grid2D(1000);
        grid2D.initialize(initializationGrid2D, state);

        for(Cell cell : initializationGrid2D.getCellList()){
            System.out.println("cellx:" + cell.getX());
            System.out.println("celly:" + cell.getY());
            int x = cell.getX() + initializationGrid2D.getTopLeftCornerX();
            int y = cell.getY() + initializationGrid2D.getTopLeftCornerY();
            System.out.println("x:" + x);
            System.out.println("y:" + y);
            assertTrue(grid2D.getCellAt(x,y).isPresent());
            assertTrue(grid2D.getCellAt(x, y).get().isAlive());
        }
    }

    @Test
    public void test(){
        SimulationOptions opt = new SimulationOptions(100000, 1000, 1000, false, 1);
        Simulator.simulate(opt);
    }


}
