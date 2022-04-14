import java.util.Optional;

public class Particle {
    private static Long currentId = 0L;
    private Long id;
    private double x;
    private double y;
    private double radius;
    private double vx;
    private double vy;
    private double vModule;
    private double mass;
    private boolean isBig;

    public Particle(double x, double y, double radius, double vx, double vy, double mass, boolean isBig) {
        this.id = currentId;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.isBig = isBig;
        this.vModule = Math.hypot(vx, vy);
        currentId++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public boolean isBig() {
        return isBig;
    }

    public void setBig(boolean big) {
        isBig = big;
    }

    public double getvModule() {
        return vModule;
    }

    public Optional<Double> computeCollisionIfExistsWith(Particle particle) {
        double[] dr = {particle.getX() - x, particle.getY() - y};
        double[] dv = {particle.getVx() - vx, particle.getVy() - vy};
        double sigma = radius + particle.getRadius();
        double vXr = dr[0] * dv[0] + dr[1] * dv[1];
        double vXv = Math.pow(dv[0], 2) + Math.pow(dv[1], 2);
        double d = Math.pow(vXr, 2) - vXv * (Math.pow(dr[0], 2) + Math.pow(dr[1], 2)
                - Math.pow(sigma, 2));

        if (vXr >= 0)
            return Optional.empty();

        if (d < 0)
            return Optional.empty();

        double t1 = -(vXr + Math.sqrt(d))/(vXv);
        double t2 = -(vXr - Math.sqrt(d))/(vXv);

        if(t1 < 0)
            return Optional.of(t2);

        return Optional.of(Math.min(t1, t2));
    }

    public Optional<Double> computeCollisionIfExistsWithWall(CollisionType type, double length) {
        double radical;
        if (type == CollisionType.PARTICLE_WITH_VWALL) {
            radical = vx > 0 ? length - radius : radius;
            return Double.compare(vx, 0D) == 0 ? Optional.empty() : Optional.of((radical - x)/vx);
        }

        radical = vy > 0 ? length - radius : radius;
        return Double.compare(vy, 0D) == 0 ? Optional.empty() : Optional.of((radical - y)/vy);
    }

    public boolean overlapsWith(Particle particle){
        return Math.pow(x - particle.getX(), 2) + Math.pow(y - particle.getY(), 2)
                <= Math.pow(radius + particle.getRadius(), 2);
    }


}
