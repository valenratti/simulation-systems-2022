public class Collision implements Comparable<Collision>{
    private Particle firstParticle;
    private Particle secondParticle;
    private CollisionType collisionType;
    private double timeToCrash;

    public Collision(Particle firstParticle, Particle secondParticle, CollisionType collisionType, Double timeToCrash) {
        this.firstParticle = firstParticle;
        this.secondParticle = secondParticle;
        this.collisionType = collisionType;
        this.timeToCrash = timeToCrash;
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

    public double getTimeToCrash() {
        return timeToCrash;
    }

    public void setTimeToCrash(double timeToCrash) {
        this.timeToCrash = timeToCrash;
    }

    public int compareTo(Collision o) {
        return Double.compare(timeToCrash, o.timeToCrash);
    }

    public void crash() {
        final double p1vx = firstParticle.getVx(), p1vy = firstParticle.getVy(), p1m = firstParticle.getMass();

        switch (collisionType) {
            case PARTICLE_WITH_VWALL:
                firstParticle.setVx(-p1vx);
                break;

            case PARTICLE_WITH_HWALL:
                firstParticle.setVy(-p1vy);
                break;

            case PARTICLE_WITH_PARTICLE:
                final double p2vx = secondParticle.getVx(), p2vy = secondParticle.getVy(), p2m = secondParticle.getMass();
                final double sigma = firstParticle.getRadius() + secondParticle.getRadius();
                final double[] dr = {secondParticle.getX() - firstParticle.getX(), secondParticle.getY() - firstParticle.getY()};
                final double[] dv = {p2vx - p1vx, p2vy - p1vy};
                final double dvXdr = dv[0] * dr[0] + dv[1] * dr[1];
                final double j = (2 * p1m * p2m * dvXdr) / (sigma * (p1m + p2m));
                firstParticle.setVx(p1vx + j * dr[0] / (sigma * p1m));
                firstParticle.setVy(p1vy + j * dr[1] / (sigma * p1m));
                secondParticle.setVx(p2vx - j * dr[0] / (sigma * p2m));
                secondParticle.setVy(p2vy - j * dr[1] / (sigma * p2m));
                secondParticle.updateVModule();
                break;
        }
        firstParticle.updateVModule();
    }

    public void decreaseTimeToCrash(double time) {
        this.timeToCrash -= time;
    }
}
