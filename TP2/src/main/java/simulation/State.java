package simulation;

import cell.Cell;
import grid.Grid;
import grid.impl.Grid2D;
import grid.impl.Grid3D;

import java.util.*;
import java.util.stream.Collectors;

public class State {
    private Map<Cell, Boolean> checkedCells;
    private Map<Cell, Boolean> cellConditionMap;
    private List<Cell> lastModified;
    private Cell center;
    private Grid grid;

    public State(int dimension, boolean is3D) {
        this.grid = is3D ? new Grid3D(dimension) : new Grid2D(dimension); // TODO: Initialization grid
        this.checkedCells = new HashMap<>();
        this.lastModified = new ArrayList<>();
        this.cellConditionMap = new HashMap<>();
    }

    public void applyChanges(List<Cell> modified){
        for(Cell cell : modified){
            if(cell.isAlive()){
                cellConditionMap.put(cell, false);
                cell.setAlive(false);
            }else{
                cellConditionMap.put(cell, true);
                cell.setAlive(true);
            }
        }

        lastModified = modified;
        checkedCells.clear();
    }

    public List<Cell> getAliveCells() {
        return cellConditionMap.keySet().stream().filter((cell) -> cellConditionMap.get(cell)).collect(Collectors.toList());
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Map<Cell, Boolean> getCheckedCells() {
        return checkedCells;
    }

    public Cell getCenter() {
        return center;
    }

    public void setCenter(Cell center) {
        this.center = center;
    }

    public List<Cell> getLastModified() {
        return lastModified;
    }

    public void setLastModified(List<Cell> lastModified) {
        this.lastModified = lastModified;
    }

    public Map<Cell, Boolean> getCellConditionMap() {
        return cellConditionMap;
    }
}
