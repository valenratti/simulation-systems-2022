import simulation.SimulationOptions;
import utils.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        final SimulationOptions simulationOptions = new SimulationOptions();
        final List<Integer> NList = Arrays.asList(105, 120, 135); // 100 < N < 150
        final List<Double> v0List = Arrays.asList(0.5, 1.0, 1.5);
        final Integer EJ4_ITERATIONS = 5; // number of iterations to calculate big particle's average

//        ej1(simulationOptions, NList);

        simulationOptions.setN(Collections.max(NList));

//        ej2(simulationOptions);

//        ej3(simulationOptions, v0List);

        ej4(simulationOptions, EJ4_ITERATIONS);

        // TODO: Ver si conviene unificar algunos ejs
    }

    private static void ej1(SimulationOptions simulationOptions, List<Integer> NList) throws IOException {
        Integer currentN = simulationOptions.getN();

        for (Integer n : NList) {
            simulationOptions.setN(n);
            Simulator.simulate(simulationOptions, 1);
        }

        simulationOptions.setN(currentN);  // leaves as it was
    }

    private static void ej2(SimulationOptions simulationOptions) throws IOException {
        Simulator.simulate(simulationOptions, 2); // TODO: ver si creamos otro metodo en Simulator, o si conviene agregar cosas al que ya tenemos
    }

    private static void ej3(SimulationOptions simulationOptions, List<Double> v0List) throws IOException {
        Double currentV = simulationOptions.getVelocityMod();
        Double ke;
        List<Pair> kineticEnergyList = new ArrayList<>();

        for (Double v : v0List) {
            simulationOptions.setVelocityMod(v);
            Simulator.simulate(simulationOptions,3);
        }

        simulationOptions.setVelocityMod(currentV);  // leaves it as it was

        System.out.println(kineticEnergyList); // TODO: print to file instead
    }

    private static void ej4(SimulationOptions simulationOptions, Integer limit) throws IOException {
        for(int i=0; i < limit; i++)
            Simulator.simulate(simulationOptions, 4); // get info of the big particle
    }

}
