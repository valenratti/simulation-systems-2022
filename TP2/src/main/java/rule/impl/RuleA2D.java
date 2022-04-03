package rule.impl;

import cell.Cell;
import grid.impl.Grid2D;
import rule.Rule;
import simulation.State;

import java.util.List;

public class RuleA2D extends Rule {
    // Conway's rule
    public RuleA2D() {
        super(1, 2, 3, 3, 3, "ruleA2D");
    }

    @Override
    public List<Cell> getNeighbours(State state, Cell cell) {
        Grid2D grid = (Grid2D) state.getGrid();
        return grid.getMooreNeighbours(cell, this.r);
    }
}
