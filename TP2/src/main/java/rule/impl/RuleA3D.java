package rule.impl;

import cell.Cell;
import grid.impl.Grid3D;
import rule.Rule;
import simulation.State;

import java.util.List;

public class RuleA3D extends Rule {
    // Builder 2 - 5-7/1/2/M
    // where 2 is the quantity of states (alive or dead)
    // and M means Moore
    public RuleA3D() {
        super(1, 5, 7, 1, 1, "ruleA3D");
    }

    @Override
    public List<Cell> getNeighbours(State state, Cell cell) {
        Grid3D grid = (Grid3D) state.getGrid();
        return grid.getMooreNeighbours(cell, this.r);
    }
}
