package simulation;

import org.kohsuke.args4j.Option;


public class SimulationOptions {

    @Option(name = "-N", usage = "Initial amount particles.")
    private Integer n = 100;

    @Option(name = "-L", usage = "Dimension length.")
    private Double length = 0.5;

    @Option(name = "-BM", usage = "Big particle mass.")
    private Double bigMass = 100.0;

    @Option(name = "-BR", usage = "Big particle radius.")
    private Double bigRadius = 0.05;

    @Option(name = "-LM", usage = "Little particle mass.")
    private Double littleMass = 0.1;

    @Option(name = "-LR", usage = "Little particle radius.")
    private Double littleRadius = 0.005;

    @Option(name = "-V", usage = "Velocity range.")
    private Double velocityRange = 0.1;

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

    public Double getBigMass() {
        return bigMass;
    }

    public void setBigMass(Double bigMass) {
        this.bigMass = bigMass;
    }

    public Double getBigRadius() {
        return bigRadius;
    }

    public void setBigRadius(Double bigRadius) {
        this.bigRadius = bigRadius;
    }

    public Double getLittleMass() {
        return littleMass;
    }

    public void setLittleMass(Double littleMass) {
        this.littleMass = littleMass;
    }

    public Double getLittleRadius() {
        return littleRadius;
    }

    public void setLittleRadius(Double littleRadius) {
        this.littleRadius = littleRadius;
    }

    public Double getVelocityRange() {
        return velocityRange;
    }

    public void setVelocityRange(Double velocityRange) {
        this.velocityRange = velocityRange;
    }
}
