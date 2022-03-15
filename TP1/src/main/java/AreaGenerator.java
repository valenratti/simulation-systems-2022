import java.util.concurrent.ThreadLocalRandom;

public class AreaGenerator {


    public static Area initializeAreaWithConfig(Config config){

        int finalM = calculateCellsPerColumn(config);
        Area area = new Area(config.getAreaLength(), finalM, config.getInteractionRadius(),
                config.isPeriodicBorderCondition());


        for(int i=0; i< config.getTotalParticles(); i++){
            generateAndAddRandomParticle(area, config);
        }
        return area;
    }

    private static int calculateCellsPerColumn(Config config){
        Double maxRadius;
        if(config.getMaxParticleRadius() != null){
            maxRadius = config.getMaxParticleRadius();
        }else {
            maxRadius = config.getParticleFixedRadius();
        }
        if(config.getCellsPerColumn() != null){
            if(config.getAreaLength() / (config.getCellsPerColumn()) > (maxRadius * 2 + config.getInteractionRadius()))
                return config.getCellsPerColumn();
        }

        int possibleM = (int) Math.floor(config.getAreaLength() / (config.getInteractionRadius() + (maxRadius * 2)));
        return possibleM == 0 ? 1 : possibleM;
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
