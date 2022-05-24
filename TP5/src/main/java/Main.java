public class Main {
    public static void main(String[] args) {
        // L > W > D
        final double L = 1.0;   // L en [1, 1.5] m
        final double W = 0.3;   // W en [0.3, 0.4] m
        final double D = 0.15;  // D en [0.15, 0.25] m

        final double minRadius = 0.01, maxRadius = 0.015;   // r en [0.01, 0.015] m
        final int N = 1000;  // number of particles TBD
        final double m = 0.01;  // kg

        Simulator.simulate(L, W, D, minRadius, maxRadius, N, m);
    }
}
