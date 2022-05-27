package model;

import java.util.ArrayList;
import java.util.List;

public class Area {
    private double width;
    private double height;
    private double rc;
    private List<Particle> particleList;
    private boolean periodicBorder;

    public Area(double width, double height) {
        this.width = width;
        this.height = height;
        this.rc = 0;
        this.periodicBorder = false;    // solo cuando salen por debajo
        this.particleList = new ArrayList<>();
    }

    public Area(double width, double height, List<Particle> particleList) {
        this.width = width;
        this.height = height;
        this.rc = 0;
        this.particleList = particleList;
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

    public void setHeight(double height) {
        this.height = height;
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
}
