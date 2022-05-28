package utils;

import java.util.Objects;

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector add(Vector vector){
        this.x += vector.getX();
        this.y += vector.getY();
        return this;
    }

    public Vector multiplyBy(double value){
        this.x *= value;
        this.y *= value;
        return this;
    }

    public Vector distance(Vector p){
        return new Vector(Math.abs(this.x - p.getX()), Math.abs(this.y - p.getY()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return x == vector.x && y == vector.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double getModule() {
        return Math.hypot(this.x, this.y);
    }

    public Vector subtract(Vector v){
        return new Vector(this.x - v.getX(), this.y - v.getY());
    }
}
