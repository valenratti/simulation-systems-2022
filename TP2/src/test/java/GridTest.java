import cell.Cell;
import grid.impl.Grid2D;
import grid.impl.InitializationGrid2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridTest {

    @Test
    public void whenInitializeGrid2D_AliveParticlesShouldMatchWithInitializationGrid(){
        InitializationGrid2D initializationGrid2D = new InitializationGrid2D(100, 500,100, 100);
        initializationGrid2D.initialize();

        Grid2D grid2D = new Grid2D(1000);
        grid2D.initialize(initializationGrid2D);

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


}
