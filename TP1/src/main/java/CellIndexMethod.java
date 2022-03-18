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
    private Map<CellCoordinates,Cell> cellMap;
    private boolean periodicBorder;
    private int cellsPerColumn;

    public CellIndexMethod(Area area, Config config) {
        this.area = area;
        this.cellsPerColumn = calculateCellsPerColumn(config);
        this.cellMap = new HashMap<>();
        for(int i = 0; i < cellsPerColumn; i++){
            for(int j = 0; j< cellsPerColumn; j++){
                cellMap.put(new CellCoordinates(i,j), new Cell(i,j,new ArrayList<>()));
            }
        }
        cellMap = new HashMap<>();
        area.getParticleList().forEach(this::calculateParticleCell);
    }


    private void calculateParticleCell(Particle particle){
        int column = (int) Math.floor(particle.getX() / (area.getLength() / this.getCellsPerColumn()));
        int row = (int) Math.floor(particle.getY() / (area.getLength() / this.getCellsPerColumn()));
        Cell cell = this.getCellMap().getOrDefault(new CellCoordinates(row, column), new Cell(row, column, new ArrayList<>()));
        particle.setCell(cell);
        cell.addParticle(particle);
        cellMap.put(new CellCoordinates(row, column), cell);
    }

    public void calculateNeighbours(Boolean periodicBorderCondition) throws FileNotFoundException {
        Map<Long, List<Long>> neighboursMap = new HashMap<>();

        for(Cell cell : cellMap.values()){
            List<Cell> neighbourCells = calculateNeighbourCells(cell.getRow(), cell.getColumn(), periodicBorderCondition).stream()
                    .map((coords) -> cellMap.get(coords)).collect(Collectors.toList());

            for (Particle particle : cell.getParticleList()) {
                List<Long> currentParticleNeighbours = neighboursMap.getOrDefault(particle.getId(), new ArrayList<>());
                for(Cell neighbourCell : neighbourCells){
                    List<Long> neighbourIds = new ArrayList<>();
                    if(neighbourCell.equals(particle.getCell())) {
                        neighbourIds.addAll(neighbourCell.getParticleList().stream()
                                .map(Particle::getId).collect(Collectors.toList()));
                    } else {
                        neighbourIds.addAll(neighbourCell.getParticleList().stream()
                                .filter((current) -> Particle.distance(particle, current, periodicBorderCondition, area.getLength()) < area.getRc())
                                .map(Particle::getId)
                                .collect(Collectors.toList()));
                    }

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

    private static int calculateCellsPerColumn(Config config){
        if(config.getCellsPerColumn() != null){
            return config.getCellsPerColumn();
        }
        Double maxRadius;
        if(config.getMaxParticleRadius() != null){
            maxRadius = config.getMaxParticleRadius();
        }else {
            maxRadius = config.getParticleFixedRadius();
        }
        if(config.getCellsPerColumn() != null){
            if(config.getAreaLength() / (config.getCellsPerColumn()) > (maxRadius * 2 + config.getInteractionRadius()))
                return config.getCellsPerColumn();
        }

        int possibleM = (int) Math.floor(config.getAreaLength() / (config.getInteractionRadius() + (maxRadius * 2)));
        return possibleM == 0 ? 1 : possibleM;
    }



    private List<CellCoordinates> calculateNeighbourCells(int i, int j, Boolean periodicBorderCondition){
        int M = this.cellsPerColumn;
        List<CellCoordinates> cellCoordinates = new ArrayList<>();
        cellCoordinates.add(new CellCoordinates(i,j));
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

    private static String writeNeighbours(Long id, final List<Long> neighbours) {
        StringBuilder list = new StringBuilder();
        neighbours.forEach(neighbourId -> {
            if(!id.equals(neighbourId)) {
                list.append(neighbourId).append("-");
            }
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

    public void setResults(Map<Long, List<Long>> results) {
        this.results = results;
    }

    public Map<CellCoordinates, Cell> getCellMap() {
        return cellMap;
    }

    public void setCellMap(Map<CellCoordinates, Cell> cellMap) {
        this.cellMap = cellMap;
    }

    public boolean isPeriodicBorder() {
        return periodicBorder;
    }

    public void setPeriodicBorder(boolean periodicBorder) {
        this.periodicBorder = periodicBorder;
    }

    public int getCellsPerColumn() {
        return cellsPerColumn;
    }

    public void setCellsPerColumn(int cellsPerColumn) {
        this.cellsPerColumn = cellsPerColumn;
    }
}
