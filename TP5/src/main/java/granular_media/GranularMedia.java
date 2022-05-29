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
        Vector force = new Vector(forceX, forceY);


        for (Wall wall : walls){
            double overlapSize = overlapSize(particle, wall);
            if (overlapSize > 0){
                double relativeVelocity = getRelativeVelocity(particle, wall);
                Vector normalAndTanForce = getNormalAndTangencialForce(overlapSize, relativeVelocity);
                force = addForceFromWall(force, wall, normalAndTanForce);
            }
        }

        particle.setPressure(Math.abs(pressure)/(2*Math.PI*particle.getRadius()));


        force = force.add(new Vector(0d, 9.8*particle.getMass()));
        return force;
    }

    private double getRelativeVelocity(Particle particle, Wall wall) {
        //Tangencial
        switch (wall.getTypeOfWall()){
            case TOP:
                return particle.getVelocity().getX();
            case RIGHT:
                return particle.getVelocity().getY();
            case BOTTOM:
                return -particle.getVelocity().getX();
            case LEFT:
                return -particle.getVelocity().getY();
        }
        return 0d;
    }

    private double overlapSize(Particle p, Wall wall){
        switch (wall.getTypeOfWall()){
//            case TOP:
//                return p.getRadius() - Math.abs(p.getY());
            case RIGHT:
                return p.getX() + p.getRadius() - boxWidth;
            case BOTTOM:
                return p.getY() + p.getRadius() - boxHeight;
            case LEFT:
                return p.getRadius() - p.getX();
        }
        return 0d;
    }

    private Vector getNormalAndTangencialForce(double overlapSize, double relativeVelocity){
        return new Vector(
                -kn * overlapSize,
                -kt * overlapSize * relativeVelocity
        );
    }

    private Vector addForceFromWall(Vector force, Wall wall, Vector normalAndTan){
        switch (wall.getTypeOfWall()){
            case TOP: // normal [0,1] ; tan [1,0]
                return force.add(new Vector(
                        normalAndTan.getY(),    // Only tan
                        -normalAndTan.getX()     // Only normal
                ));
            case RIGHT: // normal [1,0] ; tan [0,-1]
                return force.add(new Vector(
                        normalAndTan.getX(),
                        normalAndTan.getY()
                ));
            case BOTTOM: // normal [0,-1] ; tan [-1,0]
                return force.add(new Vector(
                        -normalAndTan.getY(),
                        normalAndTan.getX()
                ));
            case LEFT: // normal [-1,0] ; tan [0,1]
                return force.add(new Vector(
                        -normalAndTan.getX(),
                        -normalAndTan.getY()
                ));
        }
        return force;
    }
}
