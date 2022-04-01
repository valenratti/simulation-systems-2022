package cell.impl;

import cell.Cell;

public class Cell2D extends Cell {
    public Cell2D(int x, int y, boolean alive) {
        super(x, y, alive);
    }

    @Override
    public double distance(Cell otherCell) {
        return Math.hypot(x - otherCell.getX(), y - otherCell.getY());
    }

    @Override
    public String toString() {
        return String.format("%s %s 0", this.x, this.y);
    }
}
