import java.util.concurrent.ThreadLocalRandom;

public class AreaGenerator {


    public static Area initializeAreaWithConfig(Config config){

        Area area = new Area(config.getAreaLength(), config.getInteractionRadius(), config.isPeriodicBorderCondition());
        for(int i=0; i< config.getTotalParticles(); i++){
            area.addParticle(generateRandomParticle(area, config));
        }
        return area;
    }

    public static Particle generateRandomParticle(Area area, Config config){
        Double radius = getRadius(config.getMaxParticleRadius(), config.getParticleFixedRadius());
        double x = rand(0, area.getLength());
        double y = rand(0, area.getLength());
        return new Particle(x, y, radius);
    }

    private static Double getRadius(Double maxParticleRadius, Double particleFixedRadius) {
        return maxParticleRadius != null ? rand(0, maxParticleRadius) : particleFixedRadius;
    }

    private static double rand(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private static void generateAndAddBorderParticles(Area area, Double maxParticleRadius, Double particleFixedRadius) {
        Double radius = getRadius(maxParticleRadius, particleFixedRadius);
        double length = area.getLength() - radius;

        Particle particle = new Particle(0, 0, radius); // (0, 0)
        area.addParticle(particle);

        particle = new Particle(length, 0, radius); // (M, 0)
        area.addParticle(particle);

        particle = new Particle(0, length, radius); // (0, M)
        area.addParticle(particle);

        particle = new Particle(length, length, radius); // (M, M)
        area.addParticle(particle);
    }

}
