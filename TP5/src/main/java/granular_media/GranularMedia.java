package granular_media;

import model.ForceCalculator;
import model.Particle;
import model.Wall;
import utils.Vector;

import java.util.List;

public class GranularMedia implements ForceCalculator {
    private double kn;
    private double kt;
    private double boxWidth;
    private double boxHeight;

    public GranularMedia(double kn, double kt, double boxWidth, double boxHeight) {
        this.kn = kn;
        this.kt = kt;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    @Override
    public Vector getForceAppliedOnParticle(Particle particle, List<Particle> neighbours, List<Wall> walls){
        double forceX = 0d, forceY = 0d, pressure = 0d;
        for(Particle neighbour : neighbours) {
            double distance = particle.getDistance(neighbour);
            double normalizedXDistance = (particle.getX() - neighbour.getX()) / distance;
            double normalizedYDistance = (particle.getY() - neighbour.getY()) / distance;

            Vector tangencial = new Vector(normalizedYDistance, -normalizedXDistance);
            double overlapSize = particle.getOverlap(neighbour);
            if(overlapSize < 0) continue;
            double relativeVelocity = particle.getRelativeVelocity(neighbour , tangencial);
            double normalForceValue = - kn * overlapSize;
            double tangencialForceValue = - kt * overlapSize * relativeVelocity;

            //Fx = FN enx + FT (- eny)
            forceX += normalForceValue * normalizedXDistance + tangencialForceValue * (-normalizedYDistance);
            //Fy = FN eny + FT ( enx)
            forceY += normalForceValue * normalizedYDistance + tangencialForceValue * normalizedXDistance;
            pressure += normalForceValue;
        }
        particle.setPressure(Math.abs(pressure)/(2*Math.PI*particle.getRadius()));

        Vector force = new Vector(forceX, forceY);

        force = force.add(new Vector(0d, 9.8*particle.getMass()));
        return force;
    }
}
