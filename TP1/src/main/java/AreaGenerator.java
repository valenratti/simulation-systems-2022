import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class AreaGenerator {


    public static Area initializeAreaWithConfig(Config config){
        Area area = new Area(config.getAreaLength(), config.getCellsPerColumn(), config.getInteractionRadius(),
                config.isPeriodicBorderCondition());


        for(int i=0; i< config.getTotalParticles(); i++){
            generateAndAddRandomParticle(area, config);
        }
        return area;
    }

    private static void generateAndAddRandomParticle(Area area, Config config){
        Double radius = null;
        if(config.getMaxParticleRadius() != null){
            radius = rand(0, config.getMaxParticleRadius());
        }else {
            radius = config.getParticleFixedRadius();
        }
        double x = rand(0, area.getLength());
        double y = rand(0, area.getLength());
        Particle particle = new Particle(x, y, radius);
        area.addParticle(particle);
    }

    private static double rand(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

}
