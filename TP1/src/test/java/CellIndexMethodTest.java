import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CellIndexMethodTest {

    @Test
    void noMatterWhichMethodUsed_NeighboursShouldBeTheSame() throws FileNotFoundException {
        Config config = new Config(10.0, 2, null, 0.2,
                false, 0.1, null, null);

        Area area = AreaGenerator.initializeAreaWithConfig(config);
        area.setParticleList(this.generateRandomParticlesForArea(area, config, 1000));

        CellIndexMethod cellIndexMethod = new CellIndexMethod(area, config);
        cellIndexMethod.calculateNeighbours();

        BruteForceMethod bruteForceMethod = new BruteForceMethod(area);
        bruteForceMethod.calculateNeighbours(config.isPeriodicBorderCondition());
        for (Map.Entry<Long, List<Long>> entry : cellIndexMethod.getResults().entrySet()) {
            for (Long particleId : entry.getValue()) {
                assertTrue(bruteForceMethod.getResults().get(entry.getKey()).contains(particleId));
            }
        }
    }

    @Test
    void noMatterWhichMethodUsed_NeighboursAmountShouldBeTheSame() throws FileNotFoundException {
        Config config = new Config(10.0, 2, null, 0.2,
                false, 0.1, null, null);

        Area area = AreaGenerator.initializeAreaWithConfig(config);
        area.setParticleList(this.generateRandomParticlesForArea(area, config, 1000));

        CellIndexMethod cellIndexMethod = new CellIndexMethod(area, config);
        cellIndexMethod.calculateNeighbours();

        BruteForceMethod bruteForceMethod = new BruteForceMethod(area);
        bruteForceMethod.calculateNeighbours(config.isPeriodicBorderCondition());
        for (Map.Entry<Long, List<Long>> entry : cellIndexMethod.getResults().entrySet()) {
            assertEquals(entry.getValue().size(), bruteForceMethod.getResults().get(entry.getKey()).size());
        }
    }




    private List<Particle> generateRandomParticlesForArea(Area area, Config config, int particlesQty){
        List<Particle> particles = new ArrayList<>();
        for(int i=0; i<particlesQty; i++){
            particles.add(AreaGenerator.generateRandomParticle(area, config));
        }
        return particles;
    }
}
