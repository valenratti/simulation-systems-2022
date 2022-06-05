import cell_index_method.CIMConfig;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ej1();
    }

    public static void ej1() throws IOException {
        // L > W > D
        final double L = 1.0;   // L en [1, 1.5] m
        final double W = 0.3;   // W en [0.3, 0.4] m
        final double D = 0.15;  // D en [0.15, 0.25] m

        final double minRadius = 0.01, maxRadius = 0.015;   // r en [0.01, 0.015] m
        final int N = 300;  // number of particles TBD
        final double m = 0.01;  // kg
        final List<Double> ds = List.of(0.24);

        // dt de referencia --> dt = 0.1 * sqrt(m / kn)
        double dt = 1e-5, dt2 =1e-2;
//        dt = 0.1 * Math.sqrt(m / 1e5);
        double kn = 1e+5, kt = 2*kn;

        for(Double d : ds) {
            final CIMConfig config = new CIMConfig(L, W, d, N, minRadius, maxRadius, m, L / 10);
            Simulator.simulate(config, dt, dt2, kn, kt);
        }
    }

    static void ej4() throws IOException {
        final double L = 1.0;
        final double W = 0.3;

        final double minRadius = 0.01, maxRadius = 0.015;
        final int N = 300;
        final double m = 0.01;

        double dt = 5E-5/3, dt2 =1e-2;

        List<Double> kts = List.of(1e+5, 2e+5, 3e+5);
        final CIMConfig config = new CIMConfig(L, W, 0d, N, minRadius, maxRadius, m, L / 10);
        for(Double kt : kts) {
            Simulator.simulate(config, dt, dt2, 1e+5, kt);
        }

    }
}
