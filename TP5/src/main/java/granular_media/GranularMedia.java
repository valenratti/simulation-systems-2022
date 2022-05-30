package granular_media;

import model.ForceCalculator;
import model.Particle;
import model.Wall;
import utils.Vector;

import java.util.List;
import java.util.stream.Collectors;

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
            double normalizedXDistance = (neighbour.getX() -particle.getX()) / distance;
            double normalizedYDistance = (neighbour.getY() - particle.getY()) / distance;
            Vector normal = new Vector(normalizedXDistance, normalizedYDistance);
            Vector tangencial = new Vector(-normalizedYDistance, normalizedXDistance);
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
            if(Math.abs(forceX) >= 20 || Math.abs(forceY) >= 20){
                System.out.println("Error with particle " + particle.getId() + "neighbours " + neighbours.stream().map(Particle::getId).collect(Collectors.toList()) + " walls" + walls.stream().map(Wall::getTypeOfWall).collect(Collectors.toList()));
            }
        }
        Vector force = new Vector(forceX, forceY);

        //Force caused by interaction with walls
        for (Wall wall : walls){
            double overlapSize = overlapSize(particle, wall);
            if (overlapSize > 0){
                double relativeVelocity = getTangencialRelativeVelocity(particle, wall);
                Vector normalAndTanForce = getNormalAndTangencialForce(overlapSize, relativeVelocity);
                force = addForceFromWall(force, wall, normalAndTanForce);
            }
        }

        particle.setPressure(Math.abs(pressure)/(2*Math.PI*particle.getRadius()));

        //Finally add gravity
        force = force.add(new Vector(0d, 9.8*particle.getMass()));
        if(force.getX() >= 1000 || force.getY() >= 1000){
            System.out.println("Error with particle " + particle.getId() + "neighbours " + neighbours.stream().map(Particle::getId).collect(Collectors.toList()) + " walls" + walls.stream().map(Wall::getTypeOfWall).collect(Collectors.toList()));
        }
        return force;
    }

    private double getTangencialRelativeVelocity(Particle particle, Wall wall) {
        //Tangencial
        switch (wall.getTypeOfWall()){
            case TOP:
                return particle.getVelocity().getX();
            case RIGHT:
                return -particle.getVelocity().getY();
            case BOTTOM:
                return -particle.getVelocity().getX();
            case LEFT:
                return particle.getVelocity().getY();
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
            case TOP: // normal [0,-1] ; tan [1,0]
                return force.add(new Vector(
                        normalAndTan.getY(),    // Only tan
                        -normalAndTan.getX()     // Only normal
                ));
            case RIGHT: // normal [1,0] ; tan [0,-1]
                return force.add(new Vector(
                        normalAndTan.getX(),
                        -normalAndTan.getY()
                ));
            case BOTTOM: // normal [0,1] ; tan [-1,0]
                return force.add(new Vector(
                        -normalAndTan.getY(),
                        normalAndTan.getX()
                ));
            case LEFT: // normal [-1,0] ; tan [0,1]
                return force.add(new Vector(
                        -normalAndTan.getX(),
                        normalAndTan.getY()
                ));
        }
        return force;
    }
}
