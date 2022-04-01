package grid;

import cell.Cell;
import simulation.State;

import java.util.List;

public abstract class Grid {
    protected int dimension;

    public Grid(int dimension) {
        this.dimension = dimension;
    }

    public abstract void initialize(Grid initializationGrid, State state);

    public abstract List<Cell> getMooreNeighbours(Cell cell, int radius);

    protected abstract boolean isBorderCell(Cell cell);
}
