package system.oscilator;

import model.Particle;
import utils.Pair;

public interface System {
    Pair force(Particle particle);
    Pair forceD1(Particle particle);
    Pair forceD2(Particle particle);
    Pair forceD3(Particle particle);
    Pair analyticalSolution(Particle particle, double t);
    boolean isForceVelocityDependent();
}
