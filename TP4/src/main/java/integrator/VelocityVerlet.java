package integrator;

import model.Particle;
import system.oscilator.System;

public class VelocityVerlet implements Integrator {
    private double dt;
    private System system;

    public VelocityVerlet(double dt, System system) {
        this.dt = dt;
        this.system = system;
    }

    @Override
    public void nextStep(Particle particle) {
        // TODO
    }
}
