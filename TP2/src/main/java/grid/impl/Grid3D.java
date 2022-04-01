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
    }

    @Override
    public void initialize(Grid initializationGrid, State state) {
        InitializationGrid3D initializationGrid3D = (InitializationGrid3D) initializationGrid;
        for(int i=0; i<dimension; i++)
            for(int j=0; j<dimension; j++)
                for(int k=0; k<dimension; k++)
                cells[i][j][k] = new Cell3D(i, j, k, false);

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
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                for(int k = z - radius; k <= z + radius; k++) {
                    if (i != x || j != y) {
                        Optional<Cell> maybeNeighbour = getCellAt(i, j, k);
                        maybeNeighbour.ifPresent(neighbours::add);
                    }
                }
            }
        }
        return neighbours;
    }
}
