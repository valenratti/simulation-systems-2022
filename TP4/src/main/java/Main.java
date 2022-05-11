import utils.FileWriter;
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

        final boolean ANALYSING_BEST_DT = false;     // in order not to comment/uncomment parts of the code

        if(ANALYSING_BEST_DT) {
            List<Double> dtList = new ArrayList<>(Arrays.asList(0.01, 5e-3, 1e-3, 5e-4, 1e-4, 5e-5, 1e-5));

            List<Double> auxList, dtMseList;
            List<List<Double>> dtMseListList = new ArrayList<>();

            System.out.println("dt,\tbeeman,\tverlet,\tgpc");

            for (double dt : dtList) {
                auxList = (Simulator.simulateSystem1(m, k, gamma, A, tf, r0, v0, dt, 1e-3));
                dtMseList = new ArrayList<>(auxList);
                dtMseListList.add(dtMseList);
                System.out.println(Utils.fromDoubleListToCsvLine(dt, dtMseList, "%.3e"));
            }

            FileWriter.printDtVsMse(dtList, dtMseListList);
        }
        else {
            final double best_dt = 1e-4;
            final double dt2 = best_dt * 100;

            List<Double> mseList = Simulator.simulateSystem1(m, k, gamma, A, tf, r0, v0, best_dt, dt2);
            System.out.println(Utils.fromDoubleListToCsvLine(best_dt, mseList, "%.3e"));
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
