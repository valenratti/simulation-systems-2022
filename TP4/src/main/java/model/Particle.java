package model;

public class Particle {
    private static Long currentId = 0L;

    private Long id;
    private double x;
    private double y;
    // private double radius;
    private double vx;
    private double vy;
    private double mass;
    private double charge;

    public Particle(double x, double y, double vx, double vy, double mass, double charge, boolean idDisposable) {
        this.id = idDisposable ? null : currentId++;
        this.x = x;
        this.y = y;
        // no radius // TODO: check
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.charge = charge;
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

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public void updateState(double x, double y, double vx, double vy) {
        setX(x);
        setY(y);
        setVx(vx);
        setVy(vy);
    }
}
