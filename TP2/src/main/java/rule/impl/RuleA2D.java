package rule.impl;

import cell.Cell;
import grid.impl.Grid2D;
import rule.Rule;
import simulation.State;

import java.util.List;

public class RuleA2D extends Rule {
    public RuleA2D(int r, int a1, int a2, int d1, int d2) {
        super(r, a1, a2, d1, d2);
    }

    @Override
    public List<Cell> getNeighbours(State state, Cell cell) {
        Grid2D grid = (Grid2D) state.getGrid();
        return grid.getMooreNeighbours(cell, 1);
    }
}
