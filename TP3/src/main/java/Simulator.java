import simulation.SimulationOptions;

import java.util.List;
import java.util.PriorityQueue;

public class Simulator {

    public static void simulate(SimulationOptions options){
        final Area area = AreaInitializer.initializeArea(options);
        final PriorityQueue<Collision> collisionPriorityQueue = new PriorityQueue<>();
        final List<Particle> particleList = area.getParticles();
        final double areaLength = area.getLength(), dt = options.getDt();

        CollisionCalculator.initializeCollisions(collisionPriorityQueue, particleList, areaLength);

        Collision collision;
        Particle p1, p2;
        boolean bigParticleHitWall = false;
        double timeSinceLastLog = 0;

        logParticles(particleList);  // log initial state

        while(!bigParticleHitWall /*TODO: && another criteria: time, events qty, etc*/){
            collision = collisionPriorityQueue.peek();
            final double timeToCrash = collision.getTimeToCrash(); // TODO: chequear si es verdad que puede devolver null

            particleList.forEach(p -> p.evolve(timeToCrash)); // TODO: timeToCrash - timeSinceLastCollision ?
            timeSinceLastLog += timeToCrash;

            collisionPriorityQueue.remove();
            collisionPriorityQueue.forEach(c -> c.decreaseTimeToCrash(timeToCrash)); // all the other particles are now nearer to crash

            collision.crash();

            p1 = collision.getFirstParticle();
            p2 = collision.getSecondParticle();
            CollisionCalculator.calculateCollisionsForAParticle(collisionPriorityQueue, particleList, areaLength, p1);
            CollisionCalculator.calculateCollisionsForAParticle(collisionPriorityQueue, particleList, areaLength, p2);

            bigParticleHitWall = p1.isBig() && p2 == null; // p2 == null <==> p2 is a wall

            if(timeSinceLastLog >= dt || bigParticleHitWall) { // it is the first event after having passed dt || it is the final state
                logParticles(particleList);
                timeSinceLastLog = 0;
            }
        }
    }

    private static void logParticles(List<Particle> particleList) {
        // TODO: Implement me
    }
}
