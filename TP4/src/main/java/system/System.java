package system;

import model.Particle;
import utils.Pair;

public interface System {
    Pair force(Particle particle);
    Pair analyticalSolution(Particle particle, double t);
}
