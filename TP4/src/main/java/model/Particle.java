package model;

import java.util.List;

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
    private static final double k = 1e10;
    private double ax; //acceleration x
    private double ay; //acceleration y

    public Particle(double x, double y, double vx, double vy, double mass, double charge, boolean idDisposable) {
        this.id = idDisposable ? null : currentId++;
        this.x = x;
        this.y = y;
        // no radius // TODO: check
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.charge = charge;
        this.ax = 0;
        this.ay = 0;
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

    public double getKineticEnergy() {
        return 0.5 * this.mass * Math.pow( Math.abs( getVModule() ), 2 );
    }

    public double getElectrostaticPotentialEnergy( List<Particle> particles ) {
        double energy = 0;
        for(Particle particle : particles) {
            double distance = Math.hypot( this.x - particle.getX(), this.y - particle.getY() );
            energy += particle.charge / distance;
        }
        energy *= this.charge * Particle.k;
        return energy;
    }

    public double getTotalEnergy(List<Particle> particles) {
        return this.getKineticEnergy() + this.getElectrostaticPotentialEnergy( particles );
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }
}
