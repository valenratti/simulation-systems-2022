package grid.impl;

import cell.Cell;
import cell.impl.Cell3D;
import grid.Grid;
import simulation.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Grid3D extends Grid {
    protected Cell[][][] cells;

    public Grid3D(int dimension) {
        super(dimension);
    }

    @Override
    public void initialize(Grid initializationGrid, State state) {

    }



    public Optional<Cell> getCellAt(int x, int y, int z){
        return Optional.ofNullable(cells[x][y][z]);
    }


    /**
     * Given a cell and a radius, returns all neighbours using
     * Moore strategy
     * @param cell
     * @param radius
     * @return
     */
    @Override
    public List<Cell> getMooreNeighbours(Cell cell, int radius){
        List<Cell> neighbours = new ArrayList<>();
        Cell3D Cell3D = (Cell3D) cell;
        int x = Cell3D.getX();
        int y = Cell3D.getY();
        int z = Cell3D.getZ();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                for(int k = z - radius; k <= z + radius; k++) {
                    if (i != x || j != y) {
                        Optional<Cell> maybeNeighbour = getCellAt(i, j, k);
                        maybeNeighbour.ifPresent(neighbours::add);
                    }
                }
            }
        }
        return neighbours;
    }
}
