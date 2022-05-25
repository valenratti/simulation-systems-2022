package cell_index_method;

import java.io.File;

public class CIMConfig {
    private Double areaLength; //L
    private Integer totalParticles; //N
    private Integer cellsPerColumn; //M
    private Double interactionRadius;  //Rc
    private Boolean periodicBorderCondition;
    private Double particleFixedRadius;
    private Double maxParticleRadius;
    private File fileInput;

    public CIMConfig(Double areaLength, Integer totalParticles, Integer cellsPerColumn, Double interactionRadius, Boolean periodicBorderCondition, Double particleFixedRadius, Double maxParticleRadius, File fileInput) {
        this.areaLength = areaLength;
        this.totalParticles = totalParticles;
        this.cellsPerColumn = cellsPerColumn;
        this.interactionRadius = interactionRadius;
        this.periodicBorderCondition = periodicBorderCondition;
        this.particleFixedRadius = particleFixedRadius;
        this.maxParticleRadius = maxParticleRadius;
        this.fileInput = fileInput;
    }

    public Double getAreaLength() {
        return areaLength;
    }

    public void setAreaLength(Double areaLength) {
        this.areaLength = areaLength;
    }

    public Integer getTotalParticles() {
        return totalParticles;
    }

    public void setTotalParticles(Integer totalParticles) {
        this.totalParticles = totalParticles;
    }

    public Integer getCellsPerColumn() {
        return cellsPerColumn;
    }

    public void setCellsPerColumn(Integer cellsPerColumn) {
        this.cellsPerColumn = cellsPerColumn;
    }

    public Double getInteractionRadius() {
        return interactionRadius;
    }

    public void setInteractionRadius(Double interactionRadius) {
        this.interactionRadius = interactionRadius;
    }

    public Boolean isPeriodicBorderCondition() {
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
