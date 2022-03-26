package grid.impl;

import cell.Cell;
import cell.impl.Cell2D;
import grid.Grid;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class InitializationGrid2D extends Grid2D {

    //Amount of alive cells for the initial state
    private int initialAliveAmount;
    //Top left corner coordinates are used to attach the initialization grid in the simulation grid
    private int topLeftCornerX;
    private int topLeftCornerY;
    protected List<Cell> cellList;


    public InitializationGrid2D(int dimension, int initialAliveAmount, int topLeftCornerX, int topLeftCornerY) {
        super(dimension);
        this.initialAliveAmount = initialAliveAmount;
        this.topLeftCornerX = topLeftCornerX;
        this.topLeftCornerY = topLeftCornerY;
        cellList = new ArrayList<>();
    }

    /**
     * We'll create initialAliveAmount of alive particles
     * and assign a random position
     */
    public void initialize() {
        for(int i=0; i<initialAliveAmount; i++){
            int randX = RandomUtils.rand(0, dimension);
            int randY = RandomUtils.rand(0, dimension);

            if(getCellAt(randX, randY).isPresent()){
                getCellAt(randX,randY).get().switchState();
                cellList.remove(getCellAt(randX,randY).get());
            }else {
                Cell newCell = new Cell2D(randX, randY, true);
                cells[randX][randY] = newCell;
                cellList.add(newCell);
            }

        }
    }

    public int getInitialAliveAmount() {
        return initialAliveAmount;
    }

    public void setInitialAliveAmount(int initialAliveAmount) {
        this.initialAliveAmount = initialAliveAmount;
    }

    public int getTopLeftCornerX() {
        return topLeftCornerX;
    }

    public void setTopLeftCornerX(int topLeftCornerX) {
        this.topLeftCornerX = topLeftCornerX;
    }

    public int getTopLeftCornerY() {
        return topLeftCornerY;
    }

    public void setTopLeftCornerY(int topLeftCornerY) {
        this.topLeftCornerY = topLeftCornerY;
    }

    public List<Cell> getCellList() {
        return cellList;
    }
}
