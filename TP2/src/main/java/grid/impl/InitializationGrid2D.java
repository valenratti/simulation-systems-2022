package grid.impl;

import cell.Cell;
import cell.impl.Cell2D;
import com.sun.tools.javac.util.Pair;
import grid.Grid;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    public void initializeRandom() {
        Pair<Integer, Integer> p;
        List<Pair<Integer, Integer>> possibleCoordinates = new ArrayList<>();

        for(int i=0; i < dimension; i++)
            for(int j=0; j < dimension; j++)
                possibleCoordinates.add(new Pair<>(i, j));

        Collections.shuffle(possibleCoordinates); // sort the list randomly

        for(int i=0; i < initialAliveAmount; i++){
            p = possibleCoordinates.get(i);
            addCell(p.fst, p.snd);
        }
    }

    /**
     * We'll create initialAliveAmount of alive particles
     */
    public void initialize() {
        for(int i=0; i<dimension; i++)
            for (int j = 0; j < dimension; j++)
                addCell(i, j);
    }

    private void addCell(int x, int y) {
        Cell newCell = new Cell2D(x, y, true);
        cells[x][y] = newCell;
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

    public List<Cell> getCellList() {
        return cellList;
    }
}
