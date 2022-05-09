package system.oscilator;

import model.Particle;
import utils.Pair;

public class Gravity implements System {
    @Override
    public Pair force(Particle particle) {
        // TODO
        return new Pair(0, 0);
    }

    public Pair forceD1(Particle particle) {
        // TODO
        return new Pair(0, 0);
    }

    public Pair forceD2(Particle particle) {
        // TODO
        return new Pair(0, 0);
    }

    public Pair forceD3(Particle particle) {
        // TODO
        return new Pair(0, 0);
    }

    @Override
    public Pair analyticalSolution(Particle particle, double t) {
        return new Pair(0, 0); // TODO
    }

    @Override
    public boolean isForceVelocityDependent() {
        return false; // FIXME: check
    }
}
