import cell_index_method.CIMConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // L > W > D
        final double L = 1.0;   // L en [1, 1.5] m
        final double W = 0.3;   // W en [0.3, 0.4] m
        final double D = 0.15;  // D en [0.15, 0.25] m

        final double minRadius = 0.01, maxRadius = 0.015;   // r en [0.01, 0.015] m
        final int N = 50;  // number of particles TBD
        final double m = 0.01;  // kg

        final CIMConfig config = new CIMConfig(L, W, D, N, minRadius, maxRadius, m, L / 10);

        // dt de referencia --> dt = 0.1 * sqrt(m / kn)
        double dt = 1e-5/3, dt2 =1e-2;
//        dt = 0.1 * Math.sqrt(m / 1e5);

        Simulator.simulate(config, dt, dt2);
    }
}
