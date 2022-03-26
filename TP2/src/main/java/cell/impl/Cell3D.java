package cell.impl;

import cell.Cell;

public class Cell3D extends Cell {
    int z;
    public Cell3D(int x, int y, int z, boolean alive) {
        super(x, y, alive);
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
