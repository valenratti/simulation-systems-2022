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
        final List<Integer> NList = Arrays.asList(105, 125, 145); // 100 < N < 150
        final List<Double> v0List = Arrays.asList(0.25, 0.5, 0.75, 1.25, 1.5, 1.75);
        final Integer EJ4_ITERATIONS = 5; // number of iterations to calculate big particle's average

        ej1(simulationOptions, NList);

        simulationOptions.setN(Collections.max(NList));

        ej2(simulationOptions);

        ej3(simulationOptions, v0List);

        ej4(simulationOptions, EJ4_ITERATIONS);

        // TODO: Ver si conviene unificar algunos ejs
    }

    private static void ej1(SimulationOptions simulationOptions, List<Integer> NList) throws IOException {
        Integer currentN = simulationOptions.getN();

        for (Integer n : NList) {
            simulationOptions.setN(n);
            Simulator.simulate(simulationOptions);
        }

        simulationOptions.setN(currentN);  // leaves as it was
    }

    private static void ej2(SimulationOptions simulationOptions) throws IOException {
        Simulator.simulate(simulationOptions); // TODO: ver si creamos otro metodo en Simulator, o si conviene agregar cosas al que ya tenemos
    }

    private static void ej3(SimulationOptions simulationOptions, List<Double> v0List) throws IOException {
        Double currentV = simulationOptions.getVelocityMod();
        Double ke;
        List<Pair> kineticEnergyList = new ArrayList<>();

        for (Double v : v0List) {
            simulationOptions.setVelocityMod(v);
            ke = Simulator.getKineticEnergy(simulationOptions); //temperature
            kineticEnergyList.add(new Pair(v, ke));
        }

        simulationOptions.setVelocityMod(currentV);  // leaves it as it was

        // TODO: print values to file instead
        System.out.println(kineticEnergyList);
    }

    private static void ej4(SimulationOptions simulationOptions, Integer limit) throws IOException {
        // TODO: ver si creamos otro metodo en Simulator, o si conviene agregar cosas al que ya tenemos
        Simulator.simulate(simulationOptions); // get info of 10 small particles
        for(int i=0; i < limit; i++)
            Simulator.simulate(simulationOptions); // get info of the big particle
    }

}
