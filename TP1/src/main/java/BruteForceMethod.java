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
                if(!p1.getId().equals(p2.getId()) && distance(p1, p2, periodicBorderCondition) < area.getRc())
                    neighboursMap.getOrDefault(p1.getId(), new ArrayList<>()).add(p2.getId());

        return neighboursMap;
    }

    private double distance(Particle p1, Particle p2, Boolean periodicBorderCondition) {
        double aux, x, y;
        double x1 = p1.getX(), x2 = p2.getX(), y1 = p1.getY(), y2 = p2.getY(),
                r1 = p1.getRadius(), r2 = p2.getRadius(), l = area.getLength();

        if(periodicBorderCondition) {
            aux = Math.abs(x1 - x2);
            x = Math.min(aux, Math.abs(l - aux));

            aux = Math.abs(y1 - y2);
            y = Math.min(aux, Math.abs(l - aux));
        }
        else {
            x = x1 - x2;
            y = y1 - y2;
        }

        return Math.hypot(x, y) - (r1 + r2); //hypot(x,y) returns sqrt(x^2 + y^2)
    }
}
