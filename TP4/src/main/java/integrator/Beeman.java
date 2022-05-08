package integrator;

import model.Particle;
import system.oscilator.System;
import utils.Pair;

public class Beeman implements Integrator {
    private double dt;
    private System system;
    private Pair fPrev; // initially null

    public Beeman(double dt, System system) {
        this.dt = dt;
        this.system = system;
    }

    // receives the force value of the previous state of the particle (fPrev) and updates it
    // since this integrator needs the acceleration of the previous state
    @Override
    public void nextStep(final Particle particle) {
        final Pair f = system.force(particle);
        final double m = particle.getMass();
        final double ax = f.getX() / m, ay = f.getY() / m;
        final double vx = particle.getVx(), vy = particle.getVy();
        final double rx = particle.getX(), ry = particle.getY();

        if(fPrev == null)   // first step
            // estimamos las posiciones y velocidades anteriores con Euler evaluado en -dt
            fPrev = euler(particle, -dt, system);

        final double axPrev = fPrev.getX() / m, ayPrev = fPrev.getY() / m;

        final double rxNext = nextPosition(rx, vx, ax, axPrev);
        final double ryNext = nextPosition(ry, vy, ay, ayPrev);

        double auxVx = vx, auxVy = vy;

        if(system.isForceVelocityDependent()) {
            auxVx = predictVelocity(vx, ax, axPrev);
            auxVy = predictVelocity(vy, ay, ayPrev);
        }

        final Particle auxParticle = new Particle(rxNext, ryNext, auxVx, auxVy, m, particle.getCharge(), true);
        final Pair fNext = system.force(auxParticle);

        final double vxNext = nextVelocity(vx, ax, axPrev, fNext.getX() / m);
        final double vyNext = nextVelocity(vy, ay, ayPrev, fNext.getY() / m);

        particle.updateState(rxNext, ryNext, vxNext, vyNext);
        fPrev = fNext;
    }

    private Pair euler(Particle particle, double dt, System system) {
        final Pair f0 = system.force(particle);
        final double m = particle.getMass();
        final double ax0 = f0.getX() / m, ay0 = f0.getY();
        final double vx0 = particle.getVx(), vy0 = particle.getVy();

        final double rx = particle.getX() + dt * vx0 + dt * dt / 2 * ax0;
        final double ry = particle.getY() + dt * vy0 + dt * dt / 2 * ay0;

        final double vx = vx0 + dt * ax0;
        final double vy = vy0 + dt * ay0;

        final Particle auxParticle = new Particle(rx, ry, vx, vy, m, particle.getCharge(), true);

        return system.force(auxParticle);
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
}
