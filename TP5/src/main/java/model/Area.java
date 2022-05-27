package model;

import java.util.ArrayList;
import java.util.List;

public class Area {
    private double width;
    private double height;
    private double exitWidth;
    private List<Particle> particleList;
    private double rc;
    private boolean periodicBorder;

    public Area(double width, double height, double exitWidth, List<Particle> particleList) {
        this.width = width;
        this.height = height;
        this.exitWidth = exitWidth;
        this.particleList = particleList;
        this.rc = 0;
        this.periodicBorder = false;    // solo cuando salen por debajo
    }

    public void addParticle(Particle particle){
        particleList.add(particle);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getExitWidth() {
        return exitWidth;
    }

    public double getRc() {
        return rc;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public boolean isPeriodicBorder() {
        return periodicBorder;
    }
}
