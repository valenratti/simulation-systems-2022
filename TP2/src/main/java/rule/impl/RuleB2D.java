package rule.impl;

import cell.Cell;
import grid.impl.Grid2D;
import rule.Rule;
import simulation.State;

import java.util.List;

public class RuleB2D extends Rule {
    // Made up rule
    public RuleB2D() {
        super(2, 4, 9, 6, 6, "ruleB2D");
    }

    @Override
    public List<Cell> getNeighbours(State state, Cell cell) {
        Grid2D grid = (Grid2D) state.getGrid();
        return grid.getMooreNeighbours(cell, this.r);
    }
}
