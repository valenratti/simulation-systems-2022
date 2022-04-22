import simulation.SimulationOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Simulator {

    public static void simulate(SimulationOptions options) throws IOException {
        final Area area = AreaInitializer.initializeArea(options);
        final PriorityQueue<Collision> collisionPriorityQueue = new PriorityQueue<>();
        final List<Particle> particleList = area.getParticles();
        final double areaLength = area.getLength(), dt = options.getDt();

        CollisionCalculator.initializeCollisions(collisionPriorityQueue, particleList, areaLength);

        Collision collision;
        Particle p1, p2;
        boolean bigParticleHitWall = false;
        double timeSinceLastLog = 0;

        double totalCollisions = 0, totalTime = 0; // for exercise 3.1
        List<Double> collisionTimes = new ArrayList<>(); // for exercise 3.1

        logParticles(particleList);  // log initial state

        while(!bigParticleHitWall /*TODO: && another criteria: time, events qty, etc*/){
            collision = collisionPriorityQueue.peek();
            final double timeToCrash = collision.getTimeToCrash(); // TODO: chequear si es verdad que puede devolver null
            collisionTimes.add(timeToCrash + totalTime); // TODO: chequear si es solo timeToCrash

            particleList.forEach(p -> p.evolve(timeToCrash)); // TODO: timeToCrash - timeSinceLastCollision ?
            timeSinceLastLog += timeToCrash;

            collisionPriorityQueue.remove();
            collisionPriorityQueue.forEach(c -> c.decreaseTimeToCrash(timeToCrash)); // all the other particles are now nearer to crash

            collision.crash();
            totalCollisions++;

            p1 = collision.getFirstParticle();
            p2 = collision.getSecondParticle();
            CollisionCalculator.calculateCollisionsForAParticle(collisionPriorityQueue, particleList, areaLength, p1);
            CollisionCalculator.calculateCollisionsForAParticle(collisionPriorityQueue, particleList, areaLength, p2);

            bigParticleHitWall = p1.isBig() && p2 == null; // p2 == null <==> p2 is a wall

            if(timeSinceLastLog >= dt || bigParticleHitWall) { // it is the first event after having passed dt || it is the final state
                logParticles(particleList);
                totalTime += timeSinceLastLog; // the sum of times to crash
                timeSinceLastLog = 0;
            }
        }

        /* Exercise 3.1 */
        System.out.println("Frecuencia de colisiones: " + totalCollisions / totalTime); // TODO: print to fileA instead
        Collections.sort(collisionTimes); // ascending order
        // TODO: print collision times to the same fileA

    }

    private static void logParticles(List<Particle> particleList) throws IOException {
        FileWriter.printToFile(particleList);
    }


    /*
    * For exercise 3.3
    * The function does not simulate.
    * It initializes the simulation and calculates de kinetic energy of the system.
    * */
    public static Double getKineticEnergy(SimulationOptions options) {
        final Area area = AreaInitializer.initializeArea(options);
        final List<Particle> particleList = area.getParticles();

        return particleList.stream()
                .mapToDouble(p -> p.getMass() / 2 * Math.pow(p.getvModule(), 2))
                .average().orElse(0);
    }
}
