import simulation.SimulationOptions;
import utils.Pair;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Simulator {

    public static void simulate(SimulationOptions options, Integer ej) throws IOException {
        final Area area = AreaInitializer.initializeArea(options);
        final PriorityQueue<Collision> collisionPriorityQueue = new PriorityQueue<>();
        final List<Particle> particleList = area.getParticles();
        final double areaLength = area.getLength(), dt = options.getDt();

        CollisionCalculator.initializeCollisions(collisionPriorityQueue, particleList, areaLength);

        Collision collision;
        Particle p1, p2;
        boolean bigParticleHitWall = false;
        boolean littleParticleHitWall = false;
        double timeSinceLastLog = 0;

        double totalCollisions = 0, totalTime = 0; // for exercise 3.1
        Map<Integer, Integer> millisecondGroupedCollisions = new HashMap<>();
        logParticles(particleList);  // log initial state

        List<Double> collisionTimes = new ArrayList<>();
        List<Double> initialVelocities = particleList.stream().map(Particle::getvModule).collect(Collectors.toList());
        if(ej.equals(2)){
            FileWriter.generateVelocityFile("initialvelocities-ej3.2-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", initialVelocities);
        }

        //BigParticle
        List<Pair> bigParticlePositions = new ArrayList<>();
        Particle bigParticle = particleList.stream().filter(Particle::isBig).findFirst().get();
        bigParticlePositions.add(new Pair(bigParticle.getX(), bigParticle.getY()));
        List<Pair> bigParticleMSD = new ArrayList<>();
        double bigParticleInitialX = bigParticle.getX();
        double bigParticleInitialY = bigParticle.getY();

        //DCM Little Particle
        Particle analyzedLittleParticle = particleList.stream().filter((p) -> (p.getX() <= 1.0 && p.getY() <= 1.0)).findFirst()
                .orElseThrow(RuntimeException::new);
        double analyzedParticleInitialX = analyzedLittleParticle.getX();
        double analyzedParticleInitialY = analyzedLittleParticle.getY();
        List<Pair> littleParticleMSD = new ArrayList<>();

        if(ej.equals(4)){
            bigParticleMSD.add(new Pair(0.0, 0.0));
            littleParticleMSD.add(new Pair(0.0, 0.0));
        }

        //Kinetic energy
        Double ke = getKineticEnergy(particleList);

        while(!bigParticleHitWall && totalCollisions < options.getMaxCollisions()){
            collision = collisionPriorityQueue.peek();
            final double timeToCrash = collision.getTimeToCrash(); // TODO: chequear si es verdad que puede devolver null
            final int timeToCrashMillisecondBin = (int) Math.floor(timeToCrash*1000);

            collisionTimes.add(timeToCrash*1000);
            if(millisecondGroupedCollisions.containsKey(timeToCrashMillisecondBin)){
                millisecondGroupedCollisions.put(timeToCrashMillisecondBin, millisecondGroupedCollisions.get(timeToCrashMillisecondBin)+1);
            }else{
                millisecondGroupedCollisions.put(timeToCrashMillisecondBin, 1);
            }

            particleList.forEach(p -> p.evolve(timeToCrash)); // TODO: timeToCrash - timeSinceLastCollision ?
            timeSinceLastLog += timeToCrash;

            collisionPriorityQueue.remove();
            collisionPriorityQueue.forEach(c -> c.decreaseTimeToCrash(timeToCrash)); // all the other particles are now nearer to crash

            collision.crash();
            totalCollisions++;

            p1 = collision.getFirstParticle();
            p2 = collision.getSecondParticle();
            CollisionCalculator.calculateCollisionsForAParticle(collisionPriorityQueue, particleList, areaLength, p1);
            CollisionCalculator.calculateCollisionsForAParticle(collisionPriorityQueue, particleList, areaLength, p2);

            bigParticleHitWall = p1.isBig() && p2 == null; // p2 == null <==> p2 is a wall
            littleParticleHitWall = p1.equals(analyzedLittleParticle) && p2 == null;

            if(timeSinceLastLog >= dt || bigParticleHitWall) { // it is the first event after having passed dt || it is the final state
                totalTime += timeSinceLastLog; // the sum of times to crash
                if(ej.equals(3)) {
                    bigParticlePositions.add(new Pair(bigParticle.getX(), bigParticle.getY()));
                }else if(ej.equals(2)) {
                    FileWriter.generateVelocitiesFileDuringSimulation("smallparticlesvelocities-ej3.2-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", particleList);
                }else if(ej.equals(4)){
                    bigParticleMSD.add(new Pair(totalTime, calculateMeanSquaredDisplacement(bigParticle, bigParticleInitialX, bigParticleInitialY)));
                    if(!littleParticleHitWall) {
                        littleParticleMSD.add(new Pair(totalTime, calculateMeanSquaredDisplacement(analyzedLittleParticle, analyzedParticleInitialX, analyzedParticleInitialY)));
                    }
                }
                logParticles(particleList);
                timeSinceLastLog = 0;
            }
        }

        System.out.println("Simulation ending cause: " +
                ((bigParticleHitWall ? "The big particle hit the wall"  : "Didn't hit the wall. Max amount of collisions reached." )
                        +  "\nTime of the simulation: "  + totalTime));

        Map<Integer, Double> binProbability = new HashMap<>();
        if(ej.equals(1)) {
            double finalTotalCollisions = totalCollisions;
            millisecondGroupedCollisions.forEach((key, value) -> binProbability.put(key, (double) value / finalTotalCollisions));
            FileWriter.generateCollisionTimesFile("collisiontimes-ej3.1-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", binProbability);
            FileWriter.generateFrecuencyCollisionsTimeFile("collisionfreq-ej3.1-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", totalCollisions / totalTime);
        }
        else if(ej.equals(3)){
            FileWriter.generateBigParticlePositionFile("bigparticlepositions-ej3.3-" + LocalDateTime.now() + "-ke-" + ke + ".csv", bigParticlePositions);
        } else if(ej.equals(4)){
            FileWriter.generateParticleMSD("bigparticlemsd-ej3.4-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", bigParticleMSD);
            FileWriter.generateParticleMSD("littleparticlemsd-ej3.4-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", littleParticleMSD);
        }
    }

    private static void logParticles(List<Particle> particleList) throws IOException {
        FileWriter.printToFile(particleList);
    }


    /*
    * For exercise 3.3
    * The function does not simulate.
    * It initializes the simulation and calculates de kinetic energy of the system.
    * */
    public static Double getKineticEnergy(List<Particle> particleList) {
        return particleList.stream()
                .mapToDouble(p -> p.getMass() / 2 * Math.pow(p.getvModule(), 2))
                .average().orElse(0);
    }

    private static double calculateMeanSquaredDisplacement(Particle particle, double initialX, double initialY) {
        return Math.pow(particle.getX() - initialX, 2) + Math.pow(particle.getY() - initialY, 2);
    }
}
