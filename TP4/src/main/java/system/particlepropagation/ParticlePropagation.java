package system.particlepropagation;

import model.Particle;
import utils.FileWriter;
import utils.Pair;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParticlePropagation {
    //Configuration variables
    private final double initialV;
    private final double d; //distance between particles
    private final double l; // crystal width/length
    private final double dt; //delta time to simulate 1 step.
    private final double Q = 1e-19; // charge
    private final double k = 1e10;
    private List<Particle> crystalParticles;
    private final Particle movingParticle;
    private double time;
    private final double initialTotalEnergy;
    private boolean closenessReached;
    private static int i=0;

    //Simulation information
    private final List<Pair> particlePositions;
    private final List<Double> deltaEnergyThroughTime;


    public ParticlePropagation(double d, double dt, double initialVx, double initialVy, double mass) {
        int N = 16; // particles per row and column
        this.d = d;
        this.dt = dt;
        this.l = d * ( N - 1);
        this.time = 0;
        this.initialV = initialVx;
        double initialY = Utils.randDouble((l/2) - d, (l/2) + d);
        movingParticle = new Particle(-d, initialY, initialVx, initialVy, mass, this.Q, false);
        crystalParticles = new ArrayList<>();
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                boolean isPositiveCharge = ( i + j ) % 2 == 0;
                double currentCharge = isPositiveCharge ? this.Q : this.Q * -1;
                crystalParticles.add(new Particle(i * d, j * d, 0.0, 0.0, mass, currentCharge, false));
            }
        }
        this.particlePositions = new LinkedList<>();
        particlePositions.add(new Pair(movingParticle.getX(), movingParticle.getY()));
        this.initialTotalEnergy = movingParticle.getTotalEnergy(crystalParticles);
        this.deltaEnergyThroughTime = new ArrayList<>();
    }

    public ParticlePropagation(double d, double dt, double initialVx, double initialVy, double mass, double initialY){
        int N = 16; // particles per row and column
        this.d = d;
        this.dt = dt;
        this.l = d * ( N - 1);
        this.time = 0;
        this.initialV = initialVx;
        movingParticle = new Particle(-d, initialY, initialVx, initialVy, mass, this.Q, false);
        crystalParticles = new ArrayList<>();
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                boolean isPositiveCharge = ( i + j ) % 2 == 0;
                double currentCharge = isPositiveCharge ? this.Q : this.Q * -1;
                crystalParticles.add(new Particle(i * d, j * d, 0.0, 0.0, mass, currentCharge, false));
            }
        }
        this.particlePositions = new LinkedList<>();
        particlePositions.add(new Pair(movingParticle.getX(), movingParticle.getY()));
        this.initialTotalEnergy = movingParticle.getTotalEnergy(crystalParticles);
        this.deltaEnergyThroughTime = new ArrayList<>();
    }

    public State simulate(boolean writeResults) throws IOException {
        State currentState = State.UNFINISHED;
        while(currentState.equals(State.UNFINISHED)){
            currentState = getNextState();
        }

        if(writeResults){
            FileWriter.printParticlePropagation(this, initialV);
        }

        return currentState;
    }

    public State getNextState(){
        this.time += dt;
        Pair force = calculateForce();
        //Evolve and check new state
        evolveParticleUsingVerletMethod(force);
        State newState = getCurrentState();
        if(newState.equals(State.UNFINISHED)){
            particlePositions.add(new Pair(movingParticle.getX(), movingParticle.getY()));
        }
        this.deltaEnergyThroughTime.add(Math.abs(initialTotalEnergy - movingParticle.getTotalEnergy(crystalParticles)) / initialTotalEnergy);
        return newState;
    }

    private Pair calculateForce() {
        Pair force = new Pair(0.0, 0.0);

        //We add the force applied by all crystal particles to the moving particle
        crystalParticles.stream()
                .map(this::calculateInteractionForce)
                .forEach(force::add);

        return force.multiplyBy(this.Q * this.k);
    }

    private Pair calculateInteractionForce(Particle particle){
        double distanceX = movingParticle.getX() - particle.getX();
        double distanceY = movingParticle.getY() - particle.getY();
        double distanceAbs = Math.hypot(distanceX, distanceY);

        if(distanceAbs < d * 0.01){
            closenessReached = true;
        }
        double normalizedDistanceX = distanceX / distanceAbs;
        double normalizedDistanceY = distanceY / distanceAbs;

        double forceX = normalizedDistanceX * particle.getCharge() / Math.pow(distanceAbs, 2);
        double forceY = normalizedDistanceY * particle.getCharge() / Math.pow(distanceAbs, 2);
        return new Pair(forceX, forceY);
    }

    private State getCurrentState(){
        if(closenessReached){
            return State.INSIDE;
        }
        double x = movingParticle.getX();
        if(x < -2 * d){
            return State.LEFT_WALL;
        }else if(x > l){
            return State.RIGHT_WALL;
        }

        double y = movingParticle.getY();
        if(y < 0) {
            return State.LOWER_WALL;
        }else if (y > l){
            return State.UPPER_WALL;
        }
        return State.UNFINISHED;
    }

    private void evolveParticleUsingVerletMethod(Pair force){
        movingParticle.setAx(force.getX() / movingParticle.getMass());
        movingParticle.setAy(force.getY() / movingParticle.getMass());

        if(particlePositions.size() <= 1){
            // v1 = v0 + a * dt
            double futureVelocityX = movingParticle.getVx() + movingParticle.getAx() * dt;
            double futureVelocityY = movingParticle.getVy() + movingParticle.getAy() * dt;
            double displacedPositionX = futureVelocityX * dt + movingParticle.getAx() * 1/2 * Math.pow(dt, 2);
            double displacedPositionY = futureVelocityY * dt + movingParticle.getAy() * 1/2 * Math.pow(dt, 2);
            // ri =  ri-1 + vi.dt + a. (dt^2 / 2)
            movingParticle.updateState(movingParticle.getX() + displacedPositionX, movingParticle.getY() +
                    displacedPositionY, futureVelocityX, futureVelocityY);

        }else {
            //Verlet's new position equation
            Pair previousDtPosition = particlePositions.get(particlePositions.size() - 2);
            double futurePositionX = 2 * movingParticle.getX() - previousDtPosition.getX()
                    + movingParticle.getAx() * Math.pow(dt,2);
            double futurePositionY = 2 * movingParticle.getY() - previousDtPosition.getY()
                    + movingParticle.getAy() * Math.pow(dt,2);
            movingParticle.setX(futurePositionX);
            movingParticle.setY(futurePositionY);

            //Verlet's new velocity equation
            double futureVelocityX = (futurePositionX - previousDtPosition.getX()) * 1/(2 * dt);
            double futureVelocityY = (futurePositionY - previousDtPosition.getY()) * 1/(2 * dt);
            movingParticle.setVx(futureVelocityX);
            movingParticle.setVy(futureVelocityY);
        }

    }

    public List<Double> calculateParticleTrajectory(){
        List<Double> distances = new ArrayList<>();
        double distanceGoneThrough = 0.0;
        Pair lastPosition = null;
        for(Pair currentPosition : particlePositions){
            if(lastPosition == null){
                lastPosition = currentPosition;
                continue;
            }
            double distanceX = currentPosition.getX() - lastPosition.getX();
            double distanceY = currentPosition.getY() - lastPosition.getY();
            distanceGoneThrough += Math.hypot(distanceX, distanceY);
            distances.add(distanceGoneThrough);
        }
        return distances;
    }


    public List<Particle> getCrystalParticles() {
        return crystalParticles;
    }

    public Particle getMovingParticle() {
        return movingParticle;
    }

    public List<Pair> getParticlePositions() {
        return particlePositions;
    }

    public List<Double> getDeltaEnergyThroughTime() {
        return deltaEnergyThroughTime;
    }
}
