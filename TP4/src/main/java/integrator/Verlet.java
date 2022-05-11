package integrator;

import model.Particle;
import system.oscilator.System;
import utils.Pair;
import utils.Utils;

public class Verlet implements Integrator {
    private double dt;
    private System system;
    private Double xPrev, yPrev;
//    private Double vxPrev, vyPrev;

    public Verlet(double dt, System system) {
        this.dt = dt;
        this.system = system;
    }

    @Override
    public void nextStep(final Particle particle) {
        final Pair f = system.force(particle);
        final double m = particle.getMass();
        final double x = particle.getX(), y = particle.getY();
        final double ax = f.getX() / m, ay = f.getY() / m;

        final double dtSquared = dt * dt;

        if(xPrev == null) { // si xPrev es null, las otras 3 variables tambien los son
            Particle auxParticle = Utils.euler(particle, -dt, system);
            xPrev = auxParticle.getX();
            yPrev = auxParticle.getY();
//            vxPrev = auxParticle.getVx();
//            vyPrev = auxParticle.getVy();
        }

        final double xNext = 2 * x - xPrev + dtSquared * ax;
        final double yNext = 2 * y - yPrev + dtSquared * ay;

//        final double vx = (xNext - xPrev) / (2 * dt);
//        final double vy = (yNext - yPrev) / (2 * dt);

        particle.setX(xNext);
        particle.setY(yNext);

        xPrev = x;
        yPrev = y;
//        vxPrev = vx;
//        vyPrev = vy;
    }
}
