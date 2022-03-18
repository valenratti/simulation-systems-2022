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
        List<Long> l;

        for(Particle p1 : area.getParticleList()) {
            l = neighboursMap.getOrDefault(p1.getId(), new ArrayList<>());
            for (Particle p2 : area.getParticleList()) {
                if (!p1.getId().equals(p2.getId()) && Particle.distance(p1, p2, periodicBorderCondition, area.getLength()) < area.getRc())
                    l.add(p2.getId());
            }
            neighboursMap.put(p1.getId(), l);
        }

        return neighboursMap;
    }


}
