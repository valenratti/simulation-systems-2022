import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final double A = 1.0;   // amplitud // TODO: ver que amplitud usar

        ej1(A);

        ej2();
    }

    private static void ej1(double A) throws IOException {
        // Parametros
        final int m = 70;       // m = 70 kg
        final int k = 10^4;     // k = 10^4 N/m
        final int gamma = 100;  // gamma = 100 kg/s
        final int tf = 5;       // tf = 5 s

        // Condiciones iniciales
        final int r0 = 1;               // r(t=0) = 1 m
        final double v0 = -A * gamma / 2;  // v(t=0) = -A*gamma/(2m) m/s
    }

    private static void ej2() throws IOException {

    }
}
