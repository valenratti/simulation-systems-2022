import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForceMethod {
    private Area area;

    public BruteForceMethod(Area area) {
        this.area = area;
    }

    public Map<Long, List<Long>> calculateNeighbours(Boolean periodicBorderCondition) {
        Map<Long, List<Long>> neighboursMap = new HashMap<>();

        for(Particle p1 : area.getParticleList())
            for(Particle p2 : area.getParticleList())
                if(!p1.getId().equals(p2.getId()) && Particle.distance(p1, p2, periodicBorderCondition, area.getLength()) < area.getRc())
                    neighboursMap.getOrDefault(p1.getId(), new ArrayList<>()).add(p2.getId());

        return neighboursMap;
    }


}
