import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ej1();

//        ej2();
//        ej2_1();
//        ej2_2();
    }

    private static void ej1() throws IOException {
        // Parametros
        final double m = 70;                    // m = 70 kg
        final int k = (int) Math.pow(10, 4);    // k = 10^4 N/m
        final int gamma = 100;                  // gamma = 100 kg/s
        final double A = 1.0;                   // amplitud = r0
        final double tf = 5;                    // tf = 5 s

        // Condiciones iniciales
        final double r0 = 1;               // r(t=0) = 1 m ==> A = 1 m
        final double v0 = -A * gamma / (2 * m);  // v(t=0) = -A*gamma/(2*m) m/s

        final double dt = 0.0001;
        final double dt2 = dt*1000;

//        Simulator.simulateSystem1(m, k, gamma, A, tf, r0, v0, dt, dt*1000);

        List<Double> dtList = new ArrayList<>(Arrays.asList(0.05, 0.01, 5e-3, 1e-3, 5e-4, 1e-4, 5e-5, 1e-5));

        List<Double> mseList;

        System.out.println("dt,\tbeeman,\tverlet,\tgpc");

        for(double dtt : dtList) {
            mseList = (Simulator.simulateSystem1(m, k, gamma, A, tf, r0, v0, dtt, dtt * 100));
            System.out.println( Utils.fromDoubleListToCsvLine( Arrays.asList(dtt, mseList.get(0), mseList.get(1), mseList.get(2)), "%.3e"));
        }
    }

    private static void ej2() throws IOException {
        Simulator.simulateSystem2();
    }

    private static void ej2_1() throws IOException {
        Simulator.ej2_1();
    }

    private static void ej2_2() throws IOException {
        Simulator.ej2_2();
    }


}
