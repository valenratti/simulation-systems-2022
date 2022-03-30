package rule.impl;

import cell.Cell;
import grid.impl.Grid3D;
import rule.Rule;
import simulation.State;

import java.util.List;

public class RuleC3D extends Rule {
    // Clouds 2 - 13-26/13-14/2/M
    // where 2 is the quantity of states (alive or dead)
    // and M means Moore
    public RuleC3D() {
        super(1, 13, 26, 13, 14);
    }

    @Override
    public List<Cell> getNeighbours(State state, Cell cell) {
        Grid3D grid = (Grid3D) state.getGrid();
        return grid.getMooreNeighbours(cell, this.r);
    }
}
