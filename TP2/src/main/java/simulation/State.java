package simulation;

import cell.Cell;
import grid.Grid;
import grid.impl.Grid2D;
import grid.impl.Grid3D;

import java.util.ArrayList;
import java.util.List;

public abstract class State {
    private List<Cell> aliveCells;
    private Grid grid;

    public State(int dimension, boolean is3D) {
        this.aliveCells = new ArrayList<>();
        this.grid = is3D ? new Grid3D(dimension) : new Grid2D(dimension); // TODO: Initialization grid
    }

    public List<Cell> getAliveCells() {
        return aliveCells;
    }

    public void setAliveCells(List<Cell> aliveCells) {
        this.aliveCells = aliveCells;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
