import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Area {
    private double length;
    private double rc;
    private List<Particle> particleList;
    private boolean periodicBorder;

    public Area(double length, double rc, boolean periodicBorder) {
        this.length = length;
        this.rc = rc;
        this.periodicBorder = periodicBorder;
        this.particleList = new ArrayList<>();
    }

    public Area(double length, double rc, List<Particle> particleList, boolean periodicBorder) {
        this.length = length;
        this.rc = rc;
        this.particleList = particleList;
        this.periodicBorder = periodicBorder;
    }

    public void addParticle(Particle particle){
        particleList.add(particle);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public void setParticleList(List<Particle> particleList) {
        this.particleList = particleList;
    }

    public boolean isPeriodicBorder() {
        return periodicBorder;
    }

    public void setPeriodicBorder(boolean periodicBorder) {
        this.periodicBorder = periodicBorder;
    }
}
