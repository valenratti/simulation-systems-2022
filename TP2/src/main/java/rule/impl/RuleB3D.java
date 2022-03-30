package rule.impl;

import cell.Cell;
import grid.impl.Grid3D;
import rule.Rule;
import simulation.State;

import java.util.List;

public class RuleB3D extends Rule {
    // Architecture - 4-6/3/2/M
    // where 2 is the quantity of states (alive or dead)
    // and M means Moore
    public RuleB3D() {
        super(1, 4, 6, 3, 3);
    }

    @Override
    public List<Cell> getNeighbours(State state, Cell cell) {
        Grid3D grid = (Grid3D) state.getGrid();
        return grid.getMooreNeighbours(cell, this.r);
    }
}
