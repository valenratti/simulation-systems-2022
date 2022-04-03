import simulation.SimulationOptions;
import simulation.Simulator;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        List<Double> amountPercentages = List.of(0.5, 3.0, 10.0, 30.0, 40.0, 45.0);
        int dimension = 200;
        SimulationOptions simulationOptions;
        for (Double amount : amountPercentages) {
            int rule = 1;
            double totalParticles = Math.pow(dimension, rule > 3 ? 3 : 2);
            int n = (int) Math.floor(totalParticles * amount / 100);
            simulationOptions = new SimulationOptions(n, dimension, null, 1000, rule, false);
            Simulator.simulate(simulationOptions, amount);
        }
    }
}
