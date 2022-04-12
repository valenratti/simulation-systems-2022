import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class CollisionCalculator {

    public static void initializeCollisions(PriorityQueue<Collision> queue, List<Particle> particles, double length){
        Particle particle1, particle2;
        Optional<Double> optionalCollisionTime;

        for (int i = 0; i < particles.size(); i++) {
            particle1 = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++) {
                particle2 = particles.get(j);
                optionalCollisionTime = particle1.computeCollisionIfExistsWith(particle2);
                if (optionalCollisionTime.isPresent()) {
                    queue.add(new Collision(particle1, particle2, CollisionType.PARTICLE_WITH_PARTICLE, optionalCollisionTime.get()));
                }
            }
            optionalCollisionTime = particle1.computeCollisionIfExistsWithWall(CollisionType.PARTICLE_WITH_HWALL, length);
            if (optionalCollisionTime.isPresent()) {
                queue.add(new Collision(particle1, null, CollisionType.PARTICLE_WITH_HWALL, optionalCollisionTime.get()));
            }
            optionalCollisionTime = particle1.computeCollisionIfExistsWithWall(CollisionType.PARTICLE_WITH_VWALL, length);
            if (optionalCollisionTime.isPresent()) {
                queue.add(new Collision(particle1, null, CollisionType.PARTICLE_WITH_VWALL, optionalCollisionTime.get()));
            }
        }
    }

}
