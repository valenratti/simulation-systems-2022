import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CellIndexMethod {

    private Area area;
    private Map<Long, List<Long>> results;

    public CellIndexMethod(Area area) {
        this.area = area;
    }

    public void calculateNeighbours(Boolean periodicBorderCondition){
        Map<Long, List<Long>> neighboursMap = new HashMap<>();

        for(Cell cell : area.getCellMap().values()){
            List<Cell> neighbourCells = calculateNeighbourCells(cell.getRow(), cell.getColumn(), periodicBorderCondition).stream()
                    .map((coords) -> area.getCellMap().get(coords)).collect(Collectors.toList());

            for (Particle particle : cell.getParticleList()) {
                List<Long> currentParticleNeighbours = neighboursMap.getOrDefault(particle.getId(), new ArrayList<>());
                for(Cell neighbourCell : neighbourCells){
                    List<Long> neighboursIds = neighbourCell.getParticleList().stream()
                            .map(Particle::getId).collect(Collectors.toList());
                    neighboursIds.forEach((id) -> {
                        List<Long> ids = neighboursMap.getOrDefault(id, new ArrayList<>());
                        ids.add(particle.getId());
                        neighboursMap.put(id, ids);
                    });
                    currentParticleNeighbours.addAll(neighboursIds);
                }
                neighboursMap.put(particle.getId(), currentParticleNeighbours);
            }
        }
        results = neighboursMap;
        System.out.println("test");
    }



    private List<CellCoordinates> calculateNeighbourCells(int i, int j, Boolean periodicBorderCondition){
        int M = area.getCellsPerColumn();
        List<CellCoordinates> cellCoordinates = new ArrayList<>();

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

}
