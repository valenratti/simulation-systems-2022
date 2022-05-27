package cell_index_method;

import model.Area;
import model.Cell;
import model.CellCoordinates;
import model.Particle;
import utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CellIndexMethod {

    private Area area;
    private Map<Long, List<Long>> results;
    private Map<CellCoordinates, Cell> cellMap;
    private boolean periodicBorder;
    private int cellsPerRow;
    private int cellsPerColumn;
    private CIMConfig config;

    public CellIndexMethod(CIMConfig config) {
        this.config = config;
        this.cellsPerRow = calculateCellsPerRowOrColumn(true);
        this.cellsPerColumn = calculateCellsPerRowOrColumn(false);
        this.cellMap = new HashMap<>();

        List<Particle> particleList = new ArrayList<>();
        Particle particle;
        int counter = 0, N = config.getTotalParticles();

        for(int i = 0; i < cellsPerColumn; i++)
            for(int j = 0; j < cellsPerRow; j++) {
                cellMap.put(new CellCoordinates(i, j), new Cell(i, j, new ArrayList<>()));
                if(counter++ < N) {
                    particle = generateParticle(i, j);
                    calculateParticleCell(particle, i, j);
                    particleList.add(particle);
                }
            }

        this.area = new Area(config.getAreaWidth(), config.getAreaHeight(), particleList);
    }

    private Particle generateParticle(double i, double j) {
        double radius = Utils.rand(config.getMinParticleRadius(), config.getMaxParticleRadius());
        // centered in the cell
        double x = (j * config.getAreaWidth() / cellsPerRow) + radius;
        double y = (i * config.getAreaHeight() / cellsPerColumn) + radius;

        return new Particle(x, y, 0, 0, config.getParticleMass(), radius, false);   // TODO check: v0 = 0?
    }

    private void calculateParticleCell(Particle particle, int row, int column) {
        Cell cell = this.getCellMap().getOrDefault(new CellCoordinates(row, column), new Cell(row, column, new ArrayList<>()));
        particle.setCell(cell);
        cell.addParticle(particle);
        cellMap.put(new CellCoordinates(row, column), cell);
    }

    public void calculateNeighbours() throws FileNotFoundException {
        // TODO: check this whole method
        Map<Long, List<Long>> neighboursMap = new HashMap<>();

        for(Cell cell : cellMap.values()){
            List<Cell> neighbourCells = calculateNeighbourCells(cell.getRow(), cell.getColumn()).stream()
                    .map((coords) -> cellMap.get(coords)).collect(Collectors.toList());

            for (Particle particle : cell.getParticleList()) {
                List<Long> currentParticleNeighbours = neighboursMap.getOrDefault(particle.getId(), new ArrayList<>());
                for(Cell neighbourCell : neighbourCells) {
                    List<Long> neighbourIds = neighbourCell.getParticleList().stream()
                            .filter((current) -> Particle.distance(particle, current, area.getHeight()) < area.getRc()) // TODO: pending refactor
                            .map(Particle::getId).collect(Collectors.toList());

                    neighbourIds.forEach((id) -> {
                        List<Long> ids = neighboursMap.getOrDefault(id, new ArrayList<>());
                        ids.add(particle.getId());
                        neighboursMap.put(id, ids);
                    });
                    currentParticleNeighbours.addAll(neighbourIds);
                }
                neighboursMap.put(particle.getId(), currentParticleNeighbours);
            }
        }
        results = neighboursMap;
    }

    // true ==> row
    // false ==> column
    private int calculateCellsPerRowOrColumn(boolean perRow){
        double interactionRadius = 0;
        double maxRadius = config.getMaxParticleRadius();
        double L = perRow ? config.getAreaWidth() : config.getAreaHeight();

        int possibleM = (int) Math.floor(L / (interactionRadius + (maxRadius * 2)));

        return possibleM == 0 ? 1 : possibleM;
    }

    private List<CellCoordinates> calculateNeighbourCells(int i, int j){
        int M = this.cellsPerColumn;
        List<CellCoordinates> cellCoordinates = new ArrayList<>();
        cellCoordinates.add(new CellCoordinates(i,j));
        boolean periodicBorderCondition = false;

        if(periodicBorderCondition) {
            cellCoordinates.add(new CellCoordinates(i, (j + 1) % M)); //Derecha
            cellCoordinates.add(new CellCoordinates((i + 1) % M, (j + 1) % M)); // Diagonal abajo
            cellCoordinates.add(new CellCoordinates((i - 1 + M) % M, (j + 1) % M)); // Diagonal arriba
            cellCoordinates.add(new CellCoordinates((i - 1 + M) % M, j)); //  Arriba
        }
        else {
            boolean lastCol = j + 1 == M, firstRow = i == 0, lastRow = i + 1 == M;

            if(!lastCol) {
                cellCoordinates.add(new CellCoordinates(i, j + 1)); //Derecha
                if(!lastRow)
                    cellCoordinates.add(new CellCoordinates(i + 1, j + 1)); // Diagonal abajo
                if(!firstRow)
                    cellCoordinates.add(new CellCoordinates(i - 1, j + 1)); // Diagonal arriba
            }

            if(!firstRow)
                cellCoordinates.add(new CellCoordinates(i - 1, j)); //  Arriba
        }

        return cellCoordinates;
    }

    public void exportPositions(String path) throws FileNotFoundException {
        File file = new File(path + ".csv");
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        ps.println("x,y");
        area.getParticleList().forEach((particle) -> {
            ps.println(particle.getX() + "," + particle.getY());
        });
        ps.close();
    }

    public void exportNeighbours(String path) throws FileNotFoundException {
        File file = new File(path + ".csv");
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        ps.println("particle_id,neighbours_ids");
        results.forEach((id, list) -> {
            if(list.size() > 0) {
                ps.println(
                        (id) + "," + writeNeighbours(id, list)
                );
            }else {
                ps.println((id) + ", ");
            }
        });
        ps.close();
    }

    private String writeNeighbours(Long id, final List<Long> neighbours) {
        StringBuilder list = new StringBuilder();
        neighbours.forEach(neighbourId -> {
            if(!id.equals(neighbourId))
                list.append(neighbourId).append("-");
        });

        if(list.length() > 0)
            return list.substring(0, list.length() - 1);

        return list.toString();
    }

    public Area getArea() {
        return area;
    }

    public Map<Long, List<Long>> getResults() {
        return results;
    }

    public void setArea(Area area) {
        this.area = area;
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
