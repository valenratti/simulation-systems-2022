import simulation.SimulationOptions;
import simulation.Simulator;

public class Main {

    public static void main(String[] args) {
        SimulationOptions opt = new SimulationOptions(args);
        Simulator.simulate(opt);
    }

}
