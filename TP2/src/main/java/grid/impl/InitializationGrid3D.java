package grid.impl;

import cell.Cell;
import cell.impl.Cell2D;
import cell.impl.Cell3D;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class InitializationGrid3D extends Grid3D{

    //Amount of alive cells for the initial state
    private int initialAliveAmount;
    //Top left corner coordinates are used to attach the initialization grid in the simulation grid
    private int topLeftCornerX;
    private int topLeftCornerY;
    private int topLeftCornerZ;
    protected List<Cell> cellList;


    public InitializationGrid3D(int dimension, int initialAliveAmount, int topLeftCornerX, int topLeftCornerY, int topLeftCornerZ) {
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
            int randZ = RandomUtils.rand(0, dimension);

            if(getCellAt(randX, randY, randZ).isPresent()){
                getCellAt(randX,randY, randZ).get().switchState();
                cellList.remove(getCellAt(randX,randY, randZ).get());
                cells[randX][randY][randZ] = null;
            }else {
                Cell newCell = new Cell3D(randX, randY, randZ, true);
                cells[randX][randY][randZ] = newCell;
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

    public int getTopLeftCornerZ() {
        return topLeftCornerZ;
    }

    public void setTopLeftCornerZ(int topLeftCornerZ) {
        this.topLeftCornerZ = topLeftCornerZ;
    }

    public void setCellList(List<Cell> cellList) {
        this.cellList = cellList;
    }
}
