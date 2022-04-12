public class Collision implements Comparable<Collision>{
    private Particle firstParticle;
    private Particle secondParticle;
    private CollisionType collisionType;
    private double time;

    public Collision(Particle firstParticle, Particle secondParticle, CollisionType collisionType, Double time) {
        this.firstParticle = firstParticle;
        this.secondParticle = secondParticle;
        this.collisionType = collisionType;
        this.time = time;
    }

    public Particle getFirstParticle() {
        return firstParticle;
    }

    public void setFirstParticle(Particle firstParticle) {
        this.firstParticle = firstParticle;
    }

    public Particle getSecondParticle() {
        return secondParticle;
    }

    public void setSecondParticle(Particle secondParticle) {
        this.secondParticle = secondParticle;
    }

    public CollisionType getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(CollisionType collisionType) {
        this.collisionType = collisionType;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int compareTo(Collision o) {
        return Double.compare(time, o.time);
    }
}
