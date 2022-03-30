package grid.impl;

import cell.Cell;
import cell.impl.Cell2D;
import grid.Grid;
import simulation.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Grid2D extends Grid {
    protected Cell[][] cells;

    public Grid2D(int dimension) {
        super(dimension);
        cells = new Cell[dimension][dimension];
    }

    /**
     * Given an initialization grid, it is attached to the simulation
     * grid, so that we start with a certain amount of alive particles
     * in a limited section of space
     * @param initializationGrid
     */
    @Override
    public void initialize(Grid initializationGrid, State state) {
        InitializationGrid2D initializationGrid2D = (InitializationGrid2D) initializationGrid;
        for(int i=0; i<dimension; i++){
            for(int j=0; j<dimension; j++){
                cells[i][j] = new Cell2D(i, j, false);
            }
        }

        initializationGrid2D.getCellList().forEach((cell) -> {
            int displacedX = cell.getX() + initializationGrid2D.getTopLeftCornerX();
            int displacedY = cell.getY() + initializationGrid2D.getTopLeftCornerY();
            cells[displacedX][displacedY].setAlive(cell.isAlive());
            state.getCellConditionMap().put(getCellAt(displacedX, displacedY).get(), cell.isAlive());
        });

        state.setGrid(this);
    }

    public Optional<Cell> getCellAt(int x, int y){
        return Optional.ofNullable(cells[x][y]);
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
        int x = cell.getX();
        int y = cell.getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                    if(i != x || j != y) {
                        Optional<Cell> maybeNeighbour = getCellAt(i, j);
                        maybeNeighbour.ifPresent(neighbours::add);
                    }
            }
        }
        return neighbours;
    }
}
