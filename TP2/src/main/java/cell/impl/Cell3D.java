package cell.impl;

import cell.Cell;

import java.util.Objects;

public class Cell3D extends Cell {
    private int z;
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

    @Override
    public String toString() {
        return String.format("%s %s %s %s", this.x, this.y, this.z, this.getDistanceToCenter());
    }

    @Override
    public boolean equals(Object o) {
        Cell3D cell = (Cell3D) o;
        return super.equals(o) && z == cell.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public double distance(Cell otherCell) {
        Cell3D otherCell3D = (Cell3D) otherCell;
        return Math.sqrt(Math.pow(x - otherCell3D.getX(), 2)
                + Math.pow(y - otherCell3D.getY(), 2) + Math.pow(z - otherCell3D.getZ(), 2));
    }

}
