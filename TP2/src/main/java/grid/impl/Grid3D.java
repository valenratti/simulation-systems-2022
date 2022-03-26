package grid.impl;

import cell.Cell;
import grid.Grid;

public class Grid3D extends Grid {
    protected Cell[][][] cells;

    public Grid3D(int dimension, Cell[][][] cells) {
        super(dimension);
        this.cells = cells;
    }

    public void initialize() {

    }

    public Cell getCellAt(int x, int y, int z){
        return cells[x][y][z];
    }

    @Override
    public void initialize(Grid initializationGrid) {

    }
}
