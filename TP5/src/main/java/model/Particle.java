package model;

import utils.Vector;

public class Particle {
    private static Long currentId = 0L;

    private Long id;
    private double x;
    private double y;
    private double radius;
    private double vx;
    private double vy;
    private double mass;
    private static final double k = 1e10;
    private double ax; //acceleration x
    private double ay; //acceleration y
    private double pressure;
    private Cell cell;

    public Particle(double x, double y, double vx, double vy, double mass, double radius, boolean idDisposable) {
        this.id = idDisposable ? null : currentId++;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.ax = 0;
        this.ay = 0;
    }

    public Vector getPosition(){
        return new Vector(this.x, this.y);
    }

    public Vector getVelocity(){
        return new Vector(this.vx, this.vy);
    }

    public Long getId() {
        return id;
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

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getVModule() {
        return Math.hypot(getVx(), getVy());
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public void updateState(double x, double y, double vx, double vy) {
        setX(x);
        setY(y);
        setVx(vx);
        setVy(vy);
    }

    public double getKineticEnergy() {
        return 0.5 * this.mass * Math.pow(getVModule() , 2);
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getDistance(Particle p){
        return this.getPosition().distance(p.getPosition()).getModule();
    }

    public static double distance(Particle p1, Particle p2, double length) {
        double aux, x, y;
        double x1 = p1.getX(), x2 = p2.getX(), y1 = p1.getY(), y2 = p2.getY(),
                r1 = p1.getRadius(), r2 = p2.getRadius();
        boolean periodicBorderCondition = false;

        if(periodicBorderCondition) {
            aux = Math.abs(x1 - x2);
            x = Math.min(aux, Math.abs(length - aux));

            aux = Math.abs(y1 - y2);
            y = Math.min(aux, Math.abs(length - aux));
        }
        else {
            x = x1 - x2;
            y = y1 - y2;
        }

        return Math.hypot(x, y) - (r1 + r2); //hypot(x,y) returns sqrt(x^2 + y^2)
    }

    public double getOverlap(Particle p){
        double overlapSize = this.getRadius() + p.getRadius() - this.getDistance(p);
        return (overlapSize < 0)? 0 : overlapSize;
    }

    public double getRelativeVelocity(Particle p, Vector tangencial){
        Vector v = this.getVelocity().subtract(p.getVelocity());
        return v.getX() * tangencial.getX() + v.getY() * tangencial.getY();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        return id.equals(particle.id);
    }
}
