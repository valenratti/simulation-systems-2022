package simulation;

import cell.Cell;
import grid.Grid;

import java.util.List;

public abstract class State {
    List<Cell> aliveCells;
    Grid grid;
}
