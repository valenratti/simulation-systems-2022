import simulation.SimulationOptions;
import simulation.Simulator;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer> amountPercentages = List.of(10, 15, 25, 35, 50, 70);
        int dimension = 500;
        for(Integer amount : amountPercentages){
            double totalParticles = Math.pow(dimension, 3);
            int n = (int) Math.floor(totalParticles * amount / 100);
            SimulationOptions simulationOptions = new SimulationOptions(n, dimension, 500, 4);
            Simulator.simulate(simulationOptions, "ruleA3D-" + amount);
        }
    }

}
