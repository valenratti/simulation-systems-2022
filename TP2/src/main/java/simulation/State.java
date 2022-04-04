package simulation;

import cell.Cell;
import cell.impl.Cell2D;
import cell.impl.Cell3D;
import grid.Grid;
import grid.impl.Grid2D;
import grid.impl.Grid3D;

import java.util.*;
import java.util.stream.Collectors;

public class State {
    private Map<Cell, Boolean> checkedCells;
    private Map<Cell, Boolean> cellConditionMap;
    private long aliveCount;
    private final long totalCells;
    private List<Cell> lastModified;
    private Grid grid;
    private boolean stop;

    public State(int dimension, boolean is3D) {
        this.grid = is3D ? new Grid3D(dimension) : new Grid2D(dimension);
        this.checkedCells = new HashMap<>();
        this.lastModified = new ArrayList<>();
        this.cellConditionMap = new HashMap<>();
        this.stop = false;
        this.aliveCount = 0;
        this.totalCells = (int) Math.pow(dimension, is3D ? 3 : 2);
    }

    public void applyChanges(List<Cell> modified){
        for(Cell cell : modified){
            if(cell.isAlive()){
                cellConditionMap.put(cell, false);
                cell.setAlive(false);
                aliveCount--;
            }else{
                cellConditionMap.put(cell, true);
                cell.setAlive(true);
                aliveCount++;
                if(cell.isBorder())
                    stop = true;
            }
        }

        lastModified = modified;
        checkedCells.clear();
    }

    /**
     * Filters map keys by alive = true
     * @return
     */
    public List<Cell> getAliveCells() {
        return cellConditionMap.keySet()
                .stream()
                .filter((cell) -> cellConditionMap.get(cell)).collect(Collectors.toList());
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

    public List<Cell> getLastModified() {
        return lastModified;
    }

    public void setLastModified(List<Cell> lastModified) {
        this.lastModified = lastModified;
    }

    public Map<Cell, Boolean> getCellConditionMap() {
        return cellConditionMap;
    }

    /* returns the distance to the center of the grid of the furthest cell */
    public double getPatternRadius() {
        double maxR = 0, r;

        for(Cell cell : getAliveCells()) {
            r = cell.getDistanceToCenter();
            if(maxR < r)
                maxR = r;
        }

        return maxR;
    }

    /* returns whether it exists or not an alive border cell */
    public boolean stopCriteria() {
        return stop;
    }

    public float getAlivePercentage(){
        return (float) aliveCount / (float) totalCells;
    }

    public long getAliveCount() {
        return aliveCount;
    }

    public void setAliveCount(long aliveCount) {
        this.aliveCount = aliveCount;
    }
}
