package rule;

import cell.Cell;
import simulation.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Rule {
    protected int r, a1, a2, d1, d2;

    public Rule(int r, int a1, int a2, int d1, int d2) {
        this.r = r;
        this.a1 = a1;
        this.a2 = a2;
        this.d1 = d1;
        this.d2 = d2;
    }

    public abstract List<Cell> getNeighbours(State state, Cell cell);

    /**
     * If the cell is alive, it will remain alive if it has between a1 and a2 neighbours alive. If not, it will change its state to dead.
     * If the cell is dead, it will change its state to alive if it has between d1 and d2 neighbours alive. If not, it will remain dead.
     * */
    public boolean applyRule(State state, Cell cell) {
        List<Cell> neighbours = state.getGrid().getMooreNeighbours(cell, r);
        int aliveNeighbours = 0;

        for (Cell n : neighbours) {
            if (n.isAlive())
                aliveNeighbours++;
        }

        return cell.isAlive() ? a1 <= aliveNeighbours && aliveNeighbours <= a2
                : d1 <= aliveNeighbours && aliveNeighbours <= d2;
    }
}
