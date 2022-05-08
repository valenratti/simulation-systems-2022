import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        ej1();

        ej2();
    }

    private static void ej1() throws IOException {
        // Parametros
        final double m = 70;                    // m = 70 kg
        final int k = (int) Math.pow(10, 4);    // k = 10^4 N/m
        final int gamma = 100;                  // gamma = 100 kg/s
        final double A = 1.0;                   // amplitud // TODO: ver que amplitud usar
        final double tf = 5;                    // tf = 5 s

        // Condiciones iniciales
        final double r0 = 1;               // r(t=0) = 1 m
        final double v0 = -A * gamma / 2;  // v(t=0) = -A*gamma/(2m) m/s

        final double dt = 0.0001;
        final double dt2 = dt*1000;

        Simulator.simulateSystem1(m, k, gamma, A, tf, r0, v0, dt, dt2);
    }

    private static void ej2() throws IOException {
        Simulator.simulateSystem2();
    }
}
