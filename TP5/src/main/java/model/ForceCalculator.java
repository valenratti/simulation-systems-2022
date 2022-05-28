package model;

import utils.Vector;

import java.util.List;

public interface ForceCalculator {
    public Vector getForceAppliedOnParticle(Particle particle, List<Particle> neighbours, List<Wall> walls);
}
