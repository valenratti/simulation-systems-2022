import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class CollisionCalculator {

    public static void initializeCollisions(PriorityQueue<Collision> collisionPriorityQueue, List<Particle> particleList, double areaLength){
        Particle particle1, particle2;
        Optional<Double> optionalCollisionTime;

        for (int i = 0; i < particleList.size(); i++) {
            particle1 = particleList.get(i);
            for (int j = i + 1; j < particleList.size(); j++) {
                particle2 = particleList.get(j);
                optionalCollisionTime = particle1.computeCollisionIfExistsWith(particle2);
                if (optionalCollisionTime.isPresent())
                    collisionPriorityQueue.add(new Collision(particle1, particle2, CollisionType.PARTICLE_WITH_PARTICLE, optionalCollisionTime.get()));
            }
            collisionsWithWalls(collisionPriorityQueue, areaLength, particle1);
        }
    }

    public static void calculateCollisionsForAParticle(PriorityQueue<Collision> collisionPriorityQueue, List<Particle> particleList, double areaLength, Particle particle) {
        if (particle != null) { // not a wall
            Optional<Double> optionalCollisionTime;

            collisionPriorityQueue.removeIf(c -> c.getFirstParticle().equals(particle) || c.getSecondParticle().equals(particle));

            for (Particle p2 : particleList) {
                if (!p2.equals(particle)) {
                    optionalCollisionTime = particle.computeCollisionIfExistsWith(p2);
                    if (optionalCollisionTime.isPresent())
                        collisionPriorityQueue.add(new Collision(particle, p2, CollisionType.PARTICLE_WITH_PARTICLE, optionalCollisionTime.get()));
                }
            }

            collisionsWithWalls(collisionPriorityQueue, areaLength, particle);
        }
    }

    private static void collisionsWithWalls(PriorityQueue<Collision> collisionPriorityQueue, double areaLength, Particle particle) {
        Optional<Double> optionalCollisionTime;

        optionalCollisionTime = particle.computeCollisionIfExistsWithWall(CollisionType.PARTICLE_WITH_HWALL, areaLength);
        if (optionalCollisionTime.isPresent())
            collisionPriorityQueue.add(new Collision(particle, null, CollisionType.PARTICLE_WITH_HWALL, optionalCollisionTime.get()));

        optionalCollisionTime = particle.computeCollisionIfExistsWithWall(CollisionType.PARTICLE_WITH_VWALL, areaLength);
        if (optionalCollisionTime.isPresent())
            collisionPriorityQueue.add(new Collision(particle, null, CollisionType.PARTICLE_WITH_VWALL, optionalCollisionTime.get()));
    }
}
