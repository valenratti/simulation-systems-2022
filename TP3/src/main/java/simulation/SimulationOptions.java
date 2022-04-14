package simulation;

import org.kohsuke.args4j.Option;

public class SimulationOptions {
    @Option(name = "-N", usage = "Initial quantity of particles.")
    private Integer n = 100;

    @Option(name = "-L", usage = "Dimension.")
    private Double length = 0.5;

    @Option(name = "-BPM", usage = "Big particle mass.")
    private Double bigParticleMass = 100.0;

    @Option(name = "-BPR", usage = "Big particle radius.")
    private Double bigParticleRadius = 0.05;

    @Option(name = "-SPM", usage = "Small particle mass.")
    private Double smallParticleMass = 0.1;

    @Option(name = "-SPR", usage = "Small particle radius.")
    private Double smallParticleRadius = 0.005;

    @Option(name = "-V", usage = "Velocity module.")
    private Double velocityMod = 0.1;

    @Option(name = "-DT", usage = "dt.")
    private Double dt = 0.05;

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
}
