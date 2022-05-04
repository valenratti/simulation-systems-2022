package system;

import model.Particle;

public class DampedOscillator {
    private final double K, GAMMA, A;

    public DampedOscillator(double k, double gamma, double a) {
        this.K = k;
        this.GAMMA = gamma;
        this.A = a;
    }

    public double force(Particle particle) {
        // f = m*a = m*r2 = -k*r - gamma*r1
        return -K * particle.getX() - GAMMA * particle.getVx(); // no hay 'y' en este sistema
    }

    public double analyticalSolution(Particle particle, double t) {
        final double m = particle.getMass();

        return A * Math.exp(-GAMMA / (2 * m) * t) *
                Math.cos(Math.sqrt(K / m - Math.pow(GAMMA, 2) / (4 * Math.pow(m, 2))) * t);
    }

}
