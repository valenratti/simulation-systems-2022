package system;

import model.Particle;

public interface System {
    double force(Particle particle);
    double analyticalSolution(Particle particle, double t);
}
