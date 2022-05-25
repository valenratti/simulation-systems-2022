package model;

import java.util.List;

public class Cell {
    private int row;
    private int column;
    private List<Particle> particleList;

    public Cell(int row, int column, List<Particle> particleList) {
        this.row = row;
        this.column = column;
        this.particleList = particleList;
    }

    public void addParticle(Particle particle){
        this.particleList.add(particle);
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public void setParticleList(List<Particle> particleList) {
        this.particleList = particleList;
    }
}
