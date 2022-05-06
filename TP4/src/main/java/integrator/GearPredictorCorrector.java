package integrator;

import model.Particle;
import system.System;

// order 5
public class GearPredictorCorrector implements Integrator {
    private double dt;
    private System system;

    public GearPredictorCorrector(double dt, System system) {
        this.dt = dt;
        this.system = system;
    }

    @Override
    public void nextStep(Particle particle) {
        // TODO
    }
}
