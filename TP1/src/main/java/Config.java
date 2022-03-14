import java.io.File;

public class Config {
    private double areaLength; //L
    private int totalParticles; //N
    private int cellsPerColumn; //M
    private double interactionRadius;  //Rc
    private boolean periodicBorderCondition;
    private Double particleFixedRadius;
    private Double maxParticleRadius;
    private File fileInput;

    public Config(double areaLength, int totalParticles, int cellsPerColumn, double interactionRadius, boolean periodicBorderCondition, Double particleFixedRadius, Double maxParticleRadius, File fileInput) {
        this.areaLength = areaLength;
        this.totalParticles = totalParticles;
        this.cellsPerColumn = cellsPerColumn;
        this.interactionRadius = interactionRadius;
        this.periodicBorderCondition = periodicBorderCondition;
        this.particleFixedRadius = particleFixedRadius;
        this.maxParticleRadius = maxParticleRadius;
        this.fileInput = fileInput;
    }

    public double getAreaLength() {
        return areaLength;
    }

    public void setAreaLength(double areaLength) {
        this.areaLength = areaLength;
    }

    public int getTotalParticles() {
        return totalParticles;
    }

    public void setTotalParticles(int totalParticles) {
        this.totalParticles = totalParticles;
    }

    public int getCellsPerColumn() {
        return cellsPerColumn;
    }

    public void setCellsPerColumn(int cellsPerColumn) {
        this.cellsPerColumn = cellsPerColumn;
    }

    public double getInteractionRadius() {
        return interactionRadius;
    }

    public void setInteractionRadius(double interactionRadius) {
        this.interactionRadius = interactionRadius;
    }

    public boolean isPeriodicBorderCondition() {
        return periodicBorderCondition;
    }

    public Double getParticleFixedRadius() {
        return particleFixedRadius;
    }

    public Double getMaxParticleRadius() {
        return maxParticleRadius;
    }

    public File getFileInput() {
        return fileInput;
    }
}
