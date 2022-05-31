package model;

import java.util.List;
import java.util.Objects;

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
        if(!particle.getCell().equals(this))
            throw new RuntimeException("Particle cell differs from current cell");

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && column == cell.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
