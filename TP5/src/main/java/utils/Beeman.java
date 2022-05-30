package utils;

import model.ForceCalculator;
import model.Particle;
import model.Wall;

import java.util.List;

public class Beeman {
    private double dt;
    private boolean isForceVelocityDependent;
    private Vector fPrev; // initially null
    final private int kn = (int) 1e5;   // 10^5 N/m
    final private int kt = 2 * kn;
    private ForceCalculator forceCalculator;

    public Beeman(double dt, boolean isForceVelocityDependent, ForceCalculator forceCalculator) {
        this.dt = dt;
        this.isForceVelocityDependent = isForceVelocityDependent;
        this.forceCalculator = forceCalculator;
    }

    // receives the force value of the previous state of the particle (fPrev) and updates it
    // since this integrator needs the acceleration of the previous state
    public void nextStep(final Particle particle, List<Particle> neighbours, List<Wall> walls) {
        final Vector f = force(particle, neighbours, walls);
        final double m = particle.getMass();
        final double ax = f.getX() / m, ay = f.getY() / m;
        final double vx = particle.getVx(), vy = particle.getVy();
        final double rx = particle.getX(), ry = particle.getY();

        if(fPrev == null) {  // first step
            // estimamos las posiciones y velocidades anteriores con Euler evaluado en -dt
            fPrev = euler(particle, -dt, neighbours, walls).getPosition();
        }

        final double axPrev = fPrev.getX() / m, ayPrev = fPrev.getY() / m;

        final double rxNext = nextPosition(rx, vx, ax, axPrev);
        final double ryNext = nextPosition(ry, vy, ay, ayPrev);
        System.out.println(rxNext - particle.getX());
        System.out.println(ryNext - particle.getY());
        if(rxNext - particle.getX() >= 0.1){
            System.out.println("here");
        }
        if(ryNext - particle.getY() >= 0.1){
            System.out.println("here");
        }

        double auxVx = vx, auxVy = vy;

        if(isForceVelocityDependent) {
            auxVx = predictVelocity(vx, ax, axPrev);
            auxVy = predictVelocity(vy, ay, ayPrev);
        }

        final Particle auxParticle = new Particle(rxNext, ryNext, auxVx, auxVy, m, particle.getRadius(), true);
        final Vector fNext = force(auxParticle, neighbours, walls);

        final double vxNext = nextVelocity(vx, ax, axPrev, fNext.getX() / m);
        final double vyNext = nextVelocity(vy, ay, ayPrev, fNext.getY() / m);

        particle.updateState(rxNext, ryNext, vxNext, vyNext);
        fPrev = f;
    }

    private double nextPosition(double r, double v, double a, double aPrev) {
        final double dtSquared = dt * dt;

        return r + v * dt + (double) 2/3 * a * dtSquared - (double) 1/6 * aPrev * dtSquared;
    }

    private double predictVelocity(double v, double a, double aPrev) {
        return v + (double) 3/2 * a * dt - (double) 1/2 * aPrev * dt;
    }

    private double nextVelocity(double v, double a, double aPrev, double aNext) {
        return v + (double) 1/3 * aNext * dt + (double) 5/6 * a * dt - (double) 1/6 * aPrev * dt;
    }

    private Vector force(Particle p, List<Particle> neighbours, List<Wall> walls) {
        return forceCalculator.getForceAppliedOnParticle(p, neighbours, walls);
    }

    private Particle euler(Particle particle, double dt, List<Particle> neighbours, List<Wall> walls) {
        final Vector f0 = force(particle, neighbours, walls);
        final double m = particle.getMass();
        final double ax0 = f0.getX() / m, ay0 = f0.getY();
        final double vx0 = particle.getVx(), vy0 = particle.getVy();

        final double rx = particle.getX() + dt * vx0 + dt * dt / 2 * ax0;
        final double ry = particle.getY() + dt * vy0 + dt * dt / 2 * ay0;

        final double vx = vx0 + dt * ax0;
        final double vy = vy0 + dt * ay0;

        return new Particle(rx, ry, vx, vy, m, particle.getRadius(), true);
    }
}
