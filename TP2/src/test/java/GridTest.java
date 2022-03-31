import cell.Cell;
import cell.impl.Cell2D;
import grid.impl.Grid2D;
import grid.impl.InitializationGrid2D;
import org.junit.jupiter.api.Test;
import rule.Rule;
import rule.impl.RuleA2D;
import simulation.SimulationOptions;
import simulation.Simulator;
import simulation.State;

import java.util.*;

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
        SimulationOptions opt = new SimulationOptions(50000, 1000, 500, false, 1);
        Simulator.simulate(opt);
    }

    @Test
    public void testRuleA2D(){
        State state = new State(100, false);
        Rule rule = new RuleA2D();

        InitializationGrid2D initializationGrid2D = new InitializationGrid2D(500, 10000,100, 100);
        // don't initialize it, we are doing it manually

        Grid2D grid2D = new Grid2D(100);
        grid2D.initialize(initializationGrid2D /*useless but necessary*/, state);

        // Set some cells alive
        Cell c1 = grid2D.setCell(5,5, true, state);
        Cell c2 = grid2D.setCell(5,6, true, state); // arriba del (5, 5)
        Cell c3 = grid2D.setCell(6, 5, true, state); // a la derecha del (5, 5)

        Set<Cell> n1 = new HashSet<>(grid2D.getMooreNeighbours(c1, 1));
        Set<Cell> n2 = new HashSet<>(grid2D.getMooreNeighbours(c2, 1));
        Set<Cell> n3 = new HashSet<>(grid2D.getMooreNeighbours(c3, 1));

        // Neighbours the 3 cells have in common
        // In other words, cells that have 3 neighbours alive (c1, c2 and c3)
        n1.retainAll(n2); // intersection between n1 and n2 is now in n1
        n3.retainAll(n1); // intersection between the three cells is now in n3

        State newState = Simulator.nextState(state, rule);
        List<Cell> aliveCells = newState.getAliveCells();

        assertTrue(aliveCells.contains(c1));
        for(Cell c : n3) // (6, 6) only
            assertTrue(aliveCells.contains(c));
        assertTrue(aliveCells.contains(new Cell2D(6, 6, true)));
        assertEquals(4, aliveCells.size());
    }

}
