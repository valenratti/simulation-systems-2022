package integrator;

import model.Particle;

public interface Integrator {
    void nextStep(final Particle particle);
}
