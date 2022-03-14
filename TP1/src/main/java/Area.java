import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Area {
    private double length;
    //Amount of cells per column / row
    private int cellsPerColumn;
    private double rc;
    private List<Particle> particleList;
    private Map<CellCoordinates,Cell> cellMap;
    private boolean periodicBorder;

    public Area(double length, int m, double rc, boolean periodicBorder) {
        this.length = length;
        cellsPerColumn = m;
        this.rc = rc;
        this.periodicBorder = periodicBorder;
        this.particleList = new ArrayList<>();

        cellMap = new HashMap<>();
        for(int i = 0; i< cellsPerColumn; i++){
            for(int j = 0; j< cellsPerColumn; j++){
                cellMap.put(new CellCoordinates(i,j), new Cell(i,j,new ArrayList<Particle>()));
            }
        }
    }

    public Area(double length, int m, double rc, List<Particle> particleList, boolean periodicBorder) {
        this.length = length;
        cellsPerColumn = m;
        this.rc = rc;
        this.particleList = particleList;
        this.periodicBorder = periodicBorder;
    }

    public void addParticle(Particle particle){
        int column = (int) Math.floor(particle.getX() / (this.getLength() / this.getCellsPerColumn()));
        int row = (int) Math.floor(particle.getY() / (this.getLength() / this.getCellsPerColumn()));
        Cell cell = this.getCellMap().get(new CellCoordinates(column, row));
        particle.setCell(cell);
        cell.addParticle(particle);
        particleList.add(particle);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getCellsPerColumn() {
        return cellsPerColumn;
    }

    public void setCellsPerColumn(int cellsPerColumn) {
        this.cellsPerColumn = cellsPerColumn;
    }

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public void setParticleList(List<Particle> particleList) {
        this.particleList = particleList;
    }

    public boolean isPeriodicBorder() {
        return periodicBorder;
    }

    public void setPeriodicBorder(boolean periodicBorder) {
        this.periodicBorder = periodicBorder;
    }

    public Map<CellCoordinates, Cell> getCellMap() {
        return cellMap;
    }
}
