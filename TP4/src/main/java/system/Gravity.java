package system;

import model.Particle;
import utils.Pair;

public class Gravity implements System {
    @Override
    public Pair force(Particle particle) {
        return new Pair(0, 0); // TODO
    }

    @Override
    public Pair analyticalSolution(Particle particle, double t) {
        return new Pair(0, 0); // TODO
    }
}
