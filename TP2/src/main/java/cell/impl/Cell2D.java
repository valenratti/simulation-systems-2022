package cell.impl;

import cell.Cell;

public class Cell2D extends Cell {
    public Cell2D(int x, int y, boolean alive) {
        super(x, y, alive);
    }

    public double distanceTo(Cell otherCell) {
        return Math.sqrt(Math.pow(x - otherCell.getX(), 2) + Math.pow(y - otherCell.getY(), 2));
    }

    @Override
    public String toString() {
        return String.format("%s  %s 0", this.getX(), this.getY());
    }
}
