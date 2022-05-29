package cell_index_method;

import model.Area;
import model.Cell;
import model.CellCoordinates;
import model.Particle;
import utils.FileWriter;
import utils.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CellIndexMethod {

    private Area area;
    private Map<Particle, List<Particle>> results;
    private final Map<CellCoordinates, Cell> cellMap;
    private Set<CellCoordinates> currentOccupiedCells;
    private boolean periodicBorder;
    private final int cellsPerRow;
    private final int cellsPerColumn;
    private final double cellSideLength;
    private final CIMConfig config;

    public CellIndexMethod(CIMConfig config) {
        this.config = config;
        this.cellSideLength = config.getMaxParticleRadius() * 2;
        this.cellsPerRow = calculateCellsPerRow();
        this.cellsPerColumn = calculateCellsPerColumn();
        this.cellMap = new HashMap<>();

        List<Particle> particleList = new ArrayList<>();
        Particle particle;
        int counter = 0, N = config.getTotalParticles();

        for(int i = 0; i < cellsPerColumn; i++)
            for(int j = 0; j < cellsPerRow; j++) {
                cellMap.put(new CellCoordinates(i, j), new Cell(i, j, new ArrayList<>()));
                if(counter++ < N) {
                    particle = generateParticle(i, j);
                    addParticleToCell(particle, i, j);
                    particleList.add(particle);
                }
            }

        // extra space after exit --> L / 10
        int extraCells = (int) Math.floor(config.getHeightBelowExit() / config.getMaxParticleRadius() * 2);
        extraCells = extraCells == 0 ? 1 : extraCells;

        for(int i = cellsPerColumn; i < cellsPerColumn + extraCells; i++)
            for(int j = 0; j < cellsPerRow; j++)
                cellMap.put(new CellCoordinates(i, j), new Cell(i, j, new ArrayList<>()));

        this.area = new Area(config.getAreaWidth(), config.getAreaHeight(), config.getExitWidth(), particleList);
    }

    private Particle generateParticle(double i, double j) {
        double radius = Utils.rand(config.getMinParticleRadius(), config.getMaxParticleRadius());
        // centered in the cell
        double x = (j * config.getAreaWidth() / cellsPerRow) + radius;
        double y = (i * config.getAreaHeight() / cellsPerColumn) + radius;

        return new Particle(x, y, 0, 0, config.getParticleMass(), radius, false);   // TODO check: v0 = 0?
    }

    private void addParticleToCell(Particle particle, int row, int column) {
        Cell cell = cellMap.getOrDefault(new CellCoordinates(row, column), new Cell(row, column, new ArrayList<>()));
        particle.setCell(cell);
        cell.addParticle(particle);
        cellMap.put(new CellCoordinates(row, column), cell);
    }

    public Map<Particle, List<Particle>> calculateNeighbours() {
        // TODO: check this whole method
        Map<Particle, List<Particle>> neighboursMap = new HashMap<>();

        for(Cell cell : cellMap.values()){
            List<Cell> neighbourCells = calculateNeighbourCells(cell.getRow(), cell.getColumn())
                    .stream().map(cellMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            for (Particle particle : cell.getParticleList()) {
                List<Particle> currentParticleNeighbours = neighboursMap.getOrDefault(particle, new ArrayList<>());
                for(Cell neighbourCell : neighbourCells) {
                    List<Particle> neighbours = neighbourCell.getParticleList()
                            .stream().filter(current -> !current.equals(particle))
                            .filter((current) -> Particle.distance(particle, current, area.getHeight()) < area.getRc()) // TODO: pending refactor
                            .collect(Collectors.toList());

                    neighbours.forEach((neighbour) -> {
                        List<Particle> neighbourNeighbours = neighboursMap.getOrDefault(neighbour, new ArrayList<>());
                        neighbourNeighbours.add(particle);
                        neighboursMap.put(neighbour, neighbourNeighbours);
                    });
                    currentParticleNeighbours.addAll(neighbours);
                }
                neighboursMap.put(particle, currentParticleNeighbours);
            }
        }
        results = neighboursMap;
        return neighboursMap;
    }

    private int calculateCellsPerRow(){
        double L = config.getAreaWidth();
        int possibleM = (int) Math.floor(L / this.cellSideLength);
        return possibleM == 0 ? 1 : possibleM;
    }

    private int calculateCellsPerColumn(){
        double L = config.getAreaHeight();
        int possibleM = (int) Math.floor(L / this.cellSideLength);
        return possibleM == 0 ? 1 : possibleM;
    }

    /**
     * Given a cell (i,j), returns a List
     * of CellCoordinates with neighbours cells
     * @param i
     * @param j
     * @return
     */
    private List<CellCoordinates> calculateNeighbourCells(int i, int j){
        int M = this.cellsPerColumn;
        List<CellCoordinates> cellCoordinates = new ArrayList<>();
        //Add itself as neighbour
        cellCoordinates.add(new CellCoordinates(i,j));
        boolean lastCol = j + 1 == M, firstRow = i == 0, lastRow = i + 1 == M;

        if(!lastCol) {
            cellCoordinates.add(new CellCoordinates(i, j + 1)); //Derecha
            if(!lastRow)
                cellCoordinates.add(new CellCoordinates(i + 1, j + 1)); // Diagonal abajo
            if(!firstRow)
                cellCoordinates.add(new CellCoordinates(i - 1, j + 1)); // Diagonal arriba
        }
        if(!firstRow) {
            cellCoordinates.add(new CellCoordinates(i - 1, j)); //  Arriba
        }
        return cellCoordinates;
    }

    /**
     * Given a list of particles, tries to add the particle
     * to a certain cell determined by xy position.
     * @param particles
     */
    public void updateParticles(List<Particle> particles){
        Set<CellCoordinates> currentOccupiedCells = new HashSet<>();
        for(Particle particle : particles) {
            int row = (int) Math.floor(particle.getY() / this.cellSideLength);
            int column = (int) Math.floor(particle.getX() / this.cellSideLength);
            try {
                if (row >= 0 && row <= cellsPerColumn && column >= 0 && column <= cellsPerRow) {
                    cellMap.get(new CellCoordinates(row, column)).addParticle(particle);
                    currentOccupiedCells.add(new CellCoordinates(row, column));
                }
            } catch (Exception e){
                try {
                    FileWriter.printPositions(particles);
                }catch (IOException ex){
                    System.out.println(ex.getMessage());
                }
                System.out.println("Wrong dt. Particle with id:" + particle.getId());
                System.out.println(row + " " + column);
                System.out.println(particle.getY() + " " + particle.getX());
                System.exit(0);
            }
        }
        this.currentOccupiedCells = currentOccupiedCells;
    }

    /**
     * Clears all cells leaving them without any particle inside
     */
    public void clear(){
        cellMap.values().forEach((list) -> list.getParticleList().clear());
    }

    public Area getArea() {
        return area;
    }

    public Map<Particle, List<Particle>> getResults() {
        return results;
    }

    public Map<CellCoordinates, Cell> getCellMap() {
        return cellMap;
    }

    public boolean isPeriodicBorder() {
        return periodicBorder;
    }

    public int getCellsPerRow() {
        return cellsPerRow;
    }

    public int getCellsPerColumn() {
        return cellsPerColumn;
    }

}
