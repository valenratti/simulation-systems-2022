import java.io.IOException;

public class Simulator {
    public static void simulateSystem1(final double dt, final double dt2) throws IOException {
        // time unit --> seconds

        final double CUT_TIME = 5;

        double time = 0.0;
        int aux = 0;

        System.out.println("Initial conditions and parameters:\n" +
                "dt = " + dt + " seconds\n" +
                "dt2 = " + dt2 + " seconds (" + dt2/dt + " times dt)");

        // TODO: log initial state ?

        while(time < CUT_TIME) {
            // TODO: evolve system

            time += dt; // FIXME: antes o despues de evolucionar?
            aux++;

            if(aux == dt2 / dt) {
                // TODO: log state
                aux = 0;
            }
        }
    }

    public static void simulateSystem2() throws IOException {
    }
}
