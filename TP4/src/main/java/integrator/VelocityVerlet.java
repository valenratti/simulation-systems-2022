package integrator;

import model.Particle;
import system.oscilator.System;
import utils.Pair;

public class VelocityVerlet implements Integrator {
    private double dt;
    private System system;

    public VelocityVerlet(double dt, System system) {
        this.dt = dt;
        this.system = system;
    }

    @Override
    public void nextStep(final Particle particle) {
        final Pair f0 = system.force(particle);
        final double m = particle.getMass();
        final double x0 = particle.getX(), y0 = particle.getY();
        final double vx0 = particle.getVModule(), vy0 = particle.getVy();
        final double ax0 = f0.getX() / m, ay0 = f0.getY() / m;

        final double dtSquared = dt * dt;

        final double x = x0 + dt * vx0 + dtSquared/2 * ax0;
        final double y = y0 + dt * vy0 + dtSquared/2 * ay0;

        final Particle auxParticle = new Particle(x, y, vx0, vy0, m, particle.getCharge(), true);
        final Pair f = system.force(auxParticle);

        final double ax = f.getX() / m, ay = f.getY() / m;

        final double vx = vx0 + dt/2 * (ax0 + ax);
        final double vy = vy0 + dt/2 * (ay0 + ay);

        particle.updateState(x, y, vx, vy);
    }
}
