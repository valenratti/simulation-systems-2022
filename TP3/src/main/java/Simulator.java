import simulation.SimulationOptions;
import utils.Pair;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
        Map<BigDecimal, Integer> velocityGrouped = new HashMap<>();

        logParticles(particleList, options.getN());  // log initial state

        if(ej.equals(2)){
            Map<BigDecimal, List<Particle>> bigDecimalListMap = particleList.stream().collect(Collectors.groupingBy((p) -> {
                return BigDecimal.valueOf(p.getvModule()).setScale(1, RoundingMode.HALF_EVEN);
            }));
            Map<BigDecimal, Double> doubleMap = new HashMap<>();
            bigDecimalListMap.forEach((key, value) -> doubleMap.put(key, (double) value.size() / options.getN()));

            FileWriter.generateVelocityFile("initialvelocities-ej3.2-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", doubleMap);
        }

        //BigParticle
        List<Pair> bigParticlePositions = new ArrayList<>();
        Particle bigParticle = particleList.stream().filter(Particle::isBig).findFirst().get();
        bigParticlePositions.add(new Pair(bigParticle.getX(), bigParticle.getY()));
        List<Pair> bigParticleMSD = new ArrayList<>();
        double bigParticleInitialX = bigParticle.getX();
        double bigParticleInitialY = bigParticle.getY();

        //DCM Little Particle
        List<Particle> analyzedLittleParticles = particleList.stream()
                .filter((p) -> (!p.isBig() && p.getX() >= 2.0 && p.getX() <= 4.0 && p.getY() >= 2.0 && p.getY() <= 4.0))
                .limit(5).collect(Collectors.toList());

        Map<Long, List<Pair>> littleParticlesMSD = new HashMap<>();
        Map<Long, Pair> littleParticlesInitialPositions = new HashMap<>();
        if(ej.equals(4)) {
            analyzedLittleParticles.forEach((particle) -> {
                List<Pair> newList = new ArrayList<>();
                newList.add(new Pair(0.0, 0.0));
                littleParticlesMSD.put(particle.getId(), newList);
                littleParticlesInitialPositions.put(particle.getId(), new Pair(particle.getX(), particle.getY()));
            });
        }

        //Kinetic energy
        Double ke = getKineticEnergy(particleList);

        while(!bigParticleHitWall && totalCollisions < options.getMaxCollisions()){
            collision = collisionPriorityQueue.peek();
            final double timeToCrash = collision.getTimeToCrash();
            final int timeToCrashMillisecondBin = (int) Math.floor(timeToCrash*1000);

            if(millisecondGroupedCollisions.containsKey(timeToCrashMillisecondBin)){
                millisecondGroupedCollisions.put(timeToCrashMillisecondBin, millisecondGroupedCollisions.get(timeToCrashMillisecondBin)+1);
            }else{
                millisecondGroupedCollisions.put(timeToCrashMillisecondBin, 1);
            }

            particleList.forEach(p -> p.evolve(timeToCrash));
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


//            littleParticleHitWall = p1.getId() == analyzedLittleParticle.getId() && p2 == null;

            if(timeSinceLastLog >= dt || bigParticleHitWall) { // it is the first event after having passed dt || it is the final state
                totalTime += timeSinceLastLog; // the sum of times to crash
                if(ej.equals(3)) {
                    bigParticlePositions.add(new Pair(bigParticle.getX(), bigParticle.getY()));
                }else if(ej.equals(2)) {
                    if(totalTime >= 40.0) {
                        Map<BigDecimal, List<Particle>> bigDecimalListMap = particleList.stream().collect(Collectors.groupingBy((p) -> {
                            return BigDecimal.valueOf(p.getvModule()).setScale(1, RoundingMode.HALF_EVEN);
                        }));
                        bigDecimalListMap.forEach((key, value) -> velocityGrouped.put(key, value.size()));
                    }
                }else if(ej.equals(4)) {
                    if(p2 == null) {
                        Particle finalP1 = p1;
                        analyzedLittleParticles.stream().filter((p) -> finalP1.getId() == p.getId())
                                .collect(Collectors.toList()).forEach(analyzedLittleParticles::remove);
                    }

                    double finalTotalTime = totalTime;
                    analyzedLittleParticles.forEach((particle) -> {
                        littleParticlesMSD.get(particle.getId())
                                .add(new Pair(finalTotalTime, calculateMeanSquaredDisplacement(particle, littleParticlesInitialPositions.get(particle.getId()).getX(), littleParticlesInitialPositions.get(particle.getId()).getY())));
                    });
                    bigParticleMSD.add(new Pair(totalTime, calculateMeanSquaredDisplacement(bigParticle, bigParticleInitialX, bigParticleInitialY)));

                }
                logParticles(particleList, options.getN());
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
        }else if(ej.equals(2)){
            Map<BigDecimal, Double> doubleMap = new HashMap<>();
            int totalSize = velocityGrouped.values().stream().mapToInt((value) -> value).sum();
            velocityGrouped.forEach((key,value) -> doubleMap.put(key, (double) value / totalSize));
            FileWriter.generateVelocityFile("smallparticlesvelocities-ej3.2-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", doubleMap);
        }
        else if(ej.equals(3)){
            FileWriter.generateBigParticlePositionFile("bigparticlepositions-ej3.3-" + LocalDateTime.now() + "-ke-" + ke + ".csv", bigParticlePositions);
        } else if(ej.equals(4)){
//            FileWriter.generateParticleMSD("bigparticlemsd-ej3.4-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", bigParticleMSD);
            littleParticlesMSD.forEach((key, value) -> FileWriter.generateParticleMSD("littleparticlemsd-ej3.4-" + LocalDateTime.now() + "-n-" + options.getN() + ".csv", value));

        }

        FileWriter.reset();
    }

    private static void logParticles(List<Particle> particleList, int n) throws IOException {
        FileWriter.printToFile(particleList, n);
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
