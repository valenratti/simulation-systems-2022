package grid.impl;

import cell.Cell;
import cell.impl.Cell3D;
import grid.Grid;
import simulation.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Grid3D extends Grid {
    protected Cell[][][] cells;

    public Grid3D(int dimension) {
        super(dimension);
        cells = new Cell[dimension][dimension][dimension];
    }

    @Override
    public void initialize(Grid initializationGrid, State state) {
        InitializationGrid3D initializationGrid3D = (InitializationGrid3D) initializationGrid;
        int half = (int) Math.floor((double) dimension/2);
        Cell3D c, centerCell = new Cell3D(half, half, half, false);

        for(int i=0; i<dimension; i++) {
            for(int j=0; j<dimension; j++) {
                for (int k = 0; k < dimension; k++) {
                    c = new Cell3D(i, j, k, false);
                    c.setDistanceToCenter(c.distance(centerCell));
                    c.setBorder(isBorderCell(c));
                    cells[i][j][k] = c;
                }
            }
        }

        initializationGrid3D.getCellList().forEach((cell) -> {
            int displacedX = cell.getX() + initializationGrid3D.getTopLeftCornerX();
            int displacedY = cell.getY() + initializationGrid3D.getTopLeftCornerY();
            int displacedZ = cell.getZ() + initializationGrid3D.getTopLeftCornerZ();
            cells[displacedX][displacedY][displacedZ].setAlive(cell.isAlive());
            state.getCellConditionMap().put(getCellAt(displacedX, displacedY, displacedZ).get(), cell.isAlive());
        });

        state.setGrid(this);
    }



    public Optional<Cell> getCellAt(int x, int y, int z){
        return Optional.ofNullable(cells[x][y][z]);
    }


    /**
     * Given a cell and a radius, returns all neighbours using
     * Moore strategy
     * @param cell
     * @param radius
     * @return
     */
    @Override
    public List<Cell> getMooreNeighbours(Cell cell, int radius){
        List<Cell> neighbours = new ArrayList<>();
        Cell3D Cell3D = (Cell3D) cell;
        int x = Cell3D.getX();
        int y = Cell3D.getY();
        int z = Cell3D.getZ();

        int initialX = Math.max(x - radius, 0);
        int finalX = Math.min(x + radius, dimension-1);

        int initialY = Math.max(y - radius, 0);
        int finalY = Math.min(y + radius, dimension-1);

        int initialZ = Math.max(x - radius, 0);
        int finalZ = Math.min(x + radius, dimension-1);

        for (int i = initialX; i <= finalX; i++) {
            for (int j = initialY; j <= finalY; j++) {
                for(int k = initialZ; k <= finalZ; k++) {
                    if (i != x || j != y || k != z) {
                        Optional<Cell> maybeNeighbour = getCellAt(i, j, k);
                        maybeNeighbour.ifPresent(neighbours::add);
                    }
                }
            }
        }
        return neighbours;
    }

    @Override
    protected boolean isBorderCell(Cell cell) {
        Cell3D cell3D = (Cell3D) cell;
        int x = cell3D.getX();
        int y = cell3D.getY();
        int z = cell3D.getZ();

        return x == 0 || x == dimension || y == 0 || y == dimension || z == 0 || z == dimension;
    }
}
