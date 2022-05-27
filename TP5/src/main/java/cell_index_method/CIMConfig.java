package cell_index_method;

public class CIMConfig {
    private Double areaHeight; //L
    private Double areaWidth; //W
    private Double exitWidth; //D
    private Integer totalParticles; //N
    private Double minParticleRadius;
    private Double maxParticleRadius;
    private Double particleMass;

    public CIMConfig(Double areaHeight, Double areaWidth, Double exitWidth, Integer totalParticles, Double minParticleRadius, Double maxParticleRadius, Double particleMass) {
        this.areaHeight = areaHeight;
        this.areaWidth = areaWidth;
        this.exitWidth = exitWidth;
        this.totalParticles = totalParticles;
        this.minParticleRadius = minParticleRadius;
        this.maxParticleRadius = maxParticleRadius;
        this.particleMass = particleMass;
    }

    public Double getAreaHeight() {
        return areaHeight;
    }

    public Double getAreaWidth() {
        return areaWidth;
    }

    public Double getExitWidth() {
        return exitWidth;
    }

    public Integer getTotalParticles() {
        return totalParticles;
    }

    public Double getMinParticleRadius() {
        return minParticleRadius;
    }

    public Double getMaxParticleRadius() {
        return maxParticleRadius;
    }

    public Double getParticleMass() {
        return particleMass;
    }

}
