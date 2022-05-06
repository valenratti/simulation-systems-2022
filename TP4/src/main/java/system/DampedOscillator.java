package system;

import model.Particle;
import utils.Pair;

public class DampedOscillator implements System {
    private final double K, GAMMA, A;

    public DampedOscillator(double k, double gamma, double a) {
        this.K = k;
        this.GAMMA = gamma;
        this.A = a;
    }

    @Override
    public Pair force(Particle particle) {
        // f = m*a = m*r2 = -k*r - gamma*r1
        final double x = -K * particle.getX() - GAMMA * particle.getVx();

        return new Pair(x, 0);  // no hay fuerza en 'y' en este sistema
    }

    @Override
    public Pair analyticalSolution(Particle particle, double t) {
        final double m = particle.getMass();
        final double x = A * Math.exp(-GAMMA / (2 * m) * t) *
                Math.cos(Math.sqrt(K / m - Math.pow(GAMMA, 2) / (4 * Math.pow(m, 2))) * t);

        return new Pair(x, 0);
    }

    @Override
    public boolean isForceVelocityDependent() {
        return true;
    }

}
