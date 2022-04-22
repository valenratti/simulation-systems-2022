package simulation;

import org.kohsuke.args4j.Option;

public class SimulationOptions {
    @Option(name = "-N", usage = "Initial quantity of particles.")
    private Integer n = 101;

    @Option(name = "-L", usage = "Dimension.")
    private Double length = 6.0;

    @Option(name = "-BPM", usage = "Big particle mass.")
    private Double bigParticleMass = 2.0;

    @Option(name = "-BPR", usage = "Big particle radius.")
    private Double bigParticleRadius = 0.7;

    @Option(name = "-SPM", usage = "Small particle mass.")
    private Double smallParticleMass = 0.9;

    @Option(name = "-SPR", usage = "Small particle radius.")
    private Double smallParticleRadius = 0.2;

    @Option(name = "-V", usage = "Velocity module.")
    private Double velocityMod = 1.0;

    @Option(name = "-DT", usage = "dt.")
    private Double dt = 0.05;

    @Option(name = "-MC", usage = "Second stop criteria.")
    private Integer maxCollisions = 1000000;

    public SimulationOptions(Integer n, Double length, Double bigParticleMass, Double bigParticleRadius, Double smallParticleMass, Double smallParticleRadius, Double velocityMod, Double dt, Integer maxCollisions) {
        this.n = n;
        this.length = length;
        this.bigParticleMass = bigParticleMass;
        this.bigParticleRadius = bigParticleRadius;
        this.smallParticleMass = smallParticleMass;
        this.smallParticleRadius = smallParticleRadius;
        this.velocityMod = velocityMod;
        this.dt = dt;
        this.maxCollisions = maxCollisions;
    }

    public SimulationOptions() {
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getBigParticleMass() {
        return bigParticleMass;
    }

    public void setBigParticleMass(Double bigParticleMass) {
        this.bigParticleMass = bigParticleMass;
    }

    public Double getBigParticleRadius() {
        return bigParticleRadius;
    }

    public void setBigParticleRadius(Double bigParticleRadius) {
        this.bigParticleRadius = bigParticleRadius;
    }

    public Double getSmallParticleMass() {
        return smallParticleMass;
    }

    public void setSmallParticleMass(Double smallParticleMass) {
        this.smallParticleMass = smallParticleMass;
    }

    public Double getSmallParticleRadius() {
        return smallParticleRadius;
    }

    public void setSmallParticleRadius(Double smallParticleRadius) {
        this.smallParticleRadius = smallParticleRadius;
    }

    public Double getVelocityMod() {
        return velocityMod;
    }

    public void setVelocityMod(Double velocityMod) {
        this.velocityMod = velocityMod;
    }

    public Double getDt() {
        return dt;
    }

    public void setDt(Double dt) {
        this.dt = dt;
    }

    public Integer getMaxCollisions() {
        return maxCollisions;
    }

    public void setMaxCollisions(Integer maxCollisions) {
        this.maxCollisions = maxCollisions;
    }
}
