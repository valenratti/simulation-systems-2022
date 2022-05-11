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
        final double vx = particle.getVx(), vy = particle.getVy();
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

        // Como no tenemos v1, calculamos f1 a partir de x1 y v0. Es un "truco" que dieron en clase
        final Particle auxParticle = new Particle(xNext, yNext, vx, vy, m, particle.getCharge(), true);
        final Pair f1 = system.force(auxParticle);
        final double axNext = f1.getX() / m, ayNext = f1.getY() / m;

        final double xNextNext = 2 * xNext - x + dtSquared * axNext;
        final double yNextNext = 2 * yNext - y + dtSquared * ayNext;

        final double vxNext = (xNextNext - x) / (2 * dt);
        final double vyNext = (yNextNext - y) / (2 * dt);

        particle.updateState(xNext, yNext, vxNext, vyNext);

        xPrev = x;
        yPrev = y;
//        vxPrev = vx;
//        vyPrev = vy;
    }
}
