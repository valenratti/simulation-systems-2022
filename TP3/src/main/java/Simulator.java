import simulation.SimulationOptions;

import java.util.PriorityQueue;

public class Simulator {

    public static void simulate(SimulationOptions options){
        Area area = AreaInitializer.initializeArea(options);
        PriorityQueue<Collision> collisionPriorityQueue = new PriorityQueue<>();
        CollisionCalculator.initializeCollisions(collisionPriorityQueue, area.getParticles(), area.getLength());


    }
}
