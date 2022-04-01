package grid.impl;

import cell.Cell;
import cell.impl.Cell2D;
import cell.impl.Cell3D;
import utils.RandomUtils;
import utils.Triplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitializationGrid3D extends Grid3D{

    //Amount of alive cells for the initial state
    private int initialAliveAmount;
    //Top left corner coordinates are used to attach the initialization grid in the simulation grid
    private int topLeftCornerX;
    private int topLeftCornerY;
    private int topLeftCornerZ;
    protected List<Cell3D> cellList;


    public InitializationGrid3D(int dimension, int initialAliveAmount, int topLeftCornerX, int topLeftCornerY, int topLeftCornerZ) {
        super(dimension);
        this.initialAliveAmount = initialAliveAmount;
        this.topLeftCornerX = topLeftCornerX;
        this.topLeftCornerY = topLeftCornerY;
        this.topLeftCornerZ = topLeftCornerZ;
        cellList = new ArrayList<>();
    }

    /**
     * We'll create initialAliveAmount of alive particles
     */
    public void initialize() {
        for(int i=0; i<dimension; i++)
            for (int j = 0; j < dimension; j++)
                for(int k=0; k<dimension; k++)
                    addCell(i, j, k);
    }

    /**
     * We'll create initialAliveAmount of alive particles
     * and assign a random position
     */
    public void initializeRandom() {
        Triplet t;
        List<Triplet> possibleCoordinates = new ArrayList<>();

        for(int i=0; i < dimension; i++)
            for(int j=0; j < dimension; j++)
                for(int k=0; k<dimension; k++)
                    possibleCoordinates.add(new Triplet(i, j, k));

        Collections.shuffle(possibleCoordinates); // sort the list randomly

        for(int i=0; i < initialAliveAmount; i++){
            t = possibleCoordinates.get(i);
            addCell(t.getX(), t.getY(), t.getZ());
        }
    }

    private void addCell(int x, int y, int z) {
        Cell3D newCell = new Cell3D(x, y, z,true);
        cells[x][y][z] = newCell;
        cellList.add(newCell);
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

    public List<Cell3D> getCellList() {
        return cellList;
    }

    public int getTopLeftCornerZ() {
        return topLeftCornerZ;
    }

    public void setTopLeftCornerZ(int topLeftCornerZ) {
        this.topLeftCornerZ = topLeftCornerZ;
    }

    public void setCellList(List<Cell3D> cellList) {
        this.cellList = cellList;
    }
}
