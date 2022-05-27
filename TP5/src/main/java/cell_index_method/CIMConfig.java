package cell_index_method;

import java.io.File;

public class CIMConfig {
    private Double areaHeight; //L
    private Double areaWidth; //W
    private Integer totalParticles; //N
    private Integer cellsPerRow;
    private Integer cellsPerColumn;
    private Double minParticleRadius;
    private Double maxParticleRadius;
    private File fileInput;

    public CIMConfig(Double areaHeight, Integer totalParticles, Double minParticleRadius, Double maxParticleRadius, File fileInput) {
        this.areaHeight = areaHeight;
        this.totalParticles = totalParticles;
        this.minParticleRadius = minParticleRadius;
        this.maxParticleRadius = maxParticleRadius;
        this.fileInput = fileInput;
    }

    public Double getAreaHeight() {
        return areaHeight;
    }

    public void setAreaHeight(Double areaHeight) {
        this.areaHeight = areaHeight;
    }

    public Double getAreaWidth() {
        return areaWidth;
    }

    public void setAreaWidth(Double areaWidth) {
        this.areaWidth = areaWidth;
    }

    public Integer getTotalParticles() {
        return totalParticles;
    }

    public void setTotalParticles(Integer totalParticles) {
        this.totalParticles = totalParticles;
    }

    public Integer getCellsPerRow() {
        return cellsPerRow;
    }

    public void setCellsPerRow(Integer cellsPerRow) {
        this.cellsPerRow = cellsPerRow;
    }

    public Integer getCellsPerColumn() {
        return cellsPerColumn;
    }

    public void setCellsPerColumn(Integer cellsPerColumn) {
        this.cellsPerColumn = cellsPerColumn;
    }

    public Double getMinParticleRadius() {
        return minParticleRadius;
    }

    public Double getMaxParticleRadius() {
        return maxParticleRadius;
    }

    public File getFileInput() {
        return fileInput;
    }
}
