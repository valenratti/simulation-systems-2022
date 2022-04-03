package rule.impl;

import cell.Cell;
import grid.impl.Grid2D;
import rule.Rule;
import simulation.State;

import java.util.List;

public class RuleC2D extends Rule {
    // Made up rule
    public RuleC2D() {
        super(1, 3, 3, 2, 3, "ruleC2D");
    }

    @Override
    public List<Cell> getNeighbours(State state, Cell cell) {
        Grid2D grid = (Grid2D) state.getGrid();
        return grid.getMooreNeighbours(cell, this.r);
    }
}
