public class Particle {
    private static Long currentId = 0L;

    private Long id;
    private double x;
    private double y;
    private double radius;
    private Cell cell;

    public Particle(double x, double y, double radius) {
        this.id = currentId;
        this.x = x;
        this.y = y;
        this.radius = radius;
        currentId += 1;
    }

    public static void resetCount() {
        currentId = 0L;
    }

    public static double distance(Particle p1, Particle p2, Boolean periodicBorderCondition, double length) {
        double aux, x, y;
        double x1 = p1.getX(), x2 = p2.getX(), y1 = p1.getY(), y2 = p2.getY(),
                r1 = p1.getRadius(), r2 = p2.getRadius();

        if(periodicBorderCondition) {
            aux = Math.abs(x1 - x2);
            x = Math.min(aux, Math.abs(length - aux));

            aux = Math.abs(y1 - y2);
            y = Math.min(aux, Math.abs(length - aux));
        }
        else {
            x = x1 - x2;
            y = y1 - y2;
        }

        return Math.hypot(x, y) - (r1 + r2); //hypot(x,y) returns sqrt(x^2 + y^2)
    }

    public static Long getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(Long currentId) {
        Particle.currentId = currentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
