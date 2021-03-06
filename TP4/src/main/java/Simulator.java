import integrator.Beeman;
import integrator.GearPredictorCorrector;
import integrator.Verlet;
import model.Particle;
import system.oscilator.DampedOscillator;
import system.particlepropagation.ParticlePropagation;
import system.particlepropagation.State;
import utils.FileWriter;
import utils.Pair;
import utils.Utils;

import java.io.IOException;
import java.util.*;

public class Simulator {
    public static List<Double> simulateSystem1(double m, int k, int gamma, double A, double tf, double r0, double v0, double dt, double dt2) {
        // time unit --> seconds

        double time = 0.0;
        int aux = 0;    // aux = (time % dt2) / dt  <--> aux * dt = time + k * dt2

        final double logStep = dt2 / dt;    // dt2 = logStep * dt

        final Particle p1 = new Particle(r0, 0, v0, 0, m, 0, false);
        final Particle p2 = new Particle(r0, 0, v0, 0, m, 0, false);
        final Particle p3 = new Particle(r0, 0, v0, 0, m, 0, false);
        final Particle p4 = new Particle(r0, 0, v0, 0, m, 0, false);

        final DampedOscillator dampedOscillator = new DampedOscillator(k, gamma, A);

        final Beeman beeman = new Beeman(dt, dampedOscillator); // 1
        final Verlet verlet = new Verlet(dt, dampedOscillator);    // 2
        final GearPredictorCorrector gearPredictorCorrector = new GearPredictorCorrector(dt, dampedOscillator); // 3

        double e1 = 0, e2 = 0, e3 = 0;  // errors
        double analyticalSolutionX;

//        System.out.println("Initial conditions and parameters:\n" +
//                "dt = " + dt + " seconds\n" +
//                "dt2 = " + dt2 + " seconds (" + logStep + " times dt)\n" +
//                "cut time = " + tf);

        FileWriter.logPositions(time, Arrays.asList( p1.getX(), p2.getX(), p3.getX(), p4.getX() ));

        while(time < tf) {
            time += dt;

            beeman.nextStep(p1);
            verlet.nextStep(p2);
            gearPredictorCorrector.nextStep(p3);

            analyticalSolutionX = dampedOscillator.analyticalSolution(p4, time).getX(); // 'y' is not necessary (y=0)
            p4.setX(analyticalSolutionX);

            e1 += Utils.squaredError(p1.getX(), analyticalSolutionX);
            e2 += Utils.squaredError(p2.getX(), analyticalSolutionX);
            e3 += Utils.squaredError(p3.getX(), analyticalSolutionX);

            aux++;

            if(aux == logStep) {
                FileWriter.logPositions(time, Arrays.asList( p1.getX(), p2.getX(), p3.getX(), p4.getX() ));
                dampedOscillator.analyticalSolution(p4, time);
                aux = 0;
            }
        }

        final double n = time / dt;     // in fact it's an int, but it's not significant for the mse calculus
        final double mse1 = e1 / n, mse2 = e2 / n, mse3 = e3 / n;   // mean squared errors

        return new ArrayList<>(Arrays.asList(mse1, mse2, mse3));
    }

    public static void simulateSystem2() throws IOException {
        ParticlePropagation particlePropagation = new ParticlePropagation(1e-8, 1e-13, 1e+3, 0, 1e-27);
        particlePropagation.simulate(true);
    }

    public static void ej2_1() throws IOException {
        List<Double> dtList = Arrays.asList(1e-14, 1e-15, 1e-16, 1e-17, 1e-18);
//        double initialY = Utils.randDouble((l/2) - d, (l/2) + d);
        double d = 1e-8;
        double l = d * 15;
        double initialY = (l / 2) - d;
        double yVariation = 2 * d / 5;
        List<Double> initialYList = Arrays.asList( l/2);

        for (double currentDt : dtList) {
            List<List<Double>> energyLists = new ArrayList<>();
            Map<Double, Double> dtEnergyMap = new HashMap<>();

            for (double y : initialYList) {
                ParticlePropagation particlePropagation = new ParticlePropagation(d, currentDt, 5e+4, 0, 1e-27, y);
                particlePropagation.simulate(false);
                energyLists.add(particlePropagation.getEnergyDiffThroughTime());
            }

            int maxSize = energyLists.stream().max(Comparator.comparing(List::size)).get().size();
            for (int i = 1; i < maxSize + 1; i++) {
                int qty = 0;
                double energyValue = 0;
                for (List<Double> currentList : energyLists) {
                    try {
                        energyValue += currentList.get(i - 1);
                        qty++;
                    } catch (IndexOutOfBoundsException e) {
                        //do nothing.
                    }
                }
                dtEnergyMap.put(i * currentDt, energyValue / qty);
            }
            FileWriter.printEnergyDiffVsTime(dtEnergyMap, currentDt);
        }

        //Calculate average...
    }

    public static void ej2_2() throws IOException {
        int samplesQty = 300;
        double[] velocitiesArray = new double[]{50, 150, 250, 350, 450};
        double velocityMultiplier = 1e+2;
        double d = 1e-8;
        double dt = 1e-15;
        Map<Double, Map<State,Integer>> endStateResults = new HashMap<>();
        Map<Double, Pair> trajectoryResults = new HashMap<>();
        for (double v : velocitiesArray) {
            Map<State, Integer> endStateCount = new HashMap<>();
            List<Double> particleTrajectoryList = new ArrayList<>();
            double velocityX = v * velocityMultiplier;
            for (int i = 0; i < samplesQty; i++) {
                ParticlePropagation particlePropagation = new ParticlePropagation(d, dt, velocityX, 0.0, 1e-27);
                //Only save last sample
                State endState = particlePropagation.simulate(i == samplesQty - 1);
                if(i == samplesQty - 1) {
//                    FileWriter.printPositionsFile(particlePropagation.getParticlePositions(), velocityX);
                }
                FileWriter.reset();
                endStateCount.put(endState, endStateCount.containsKey(endState) ? endStateCount.get(endState) + 1 : 1);
                if(endState.equals(State.INSIDE)) {
                    particleTrajectoryList.add(particlePropagation.calculateParticleTrajectory());
                }
            }
            endStateResults.put(velocityX, endStateCount);
            FileWriter.printParticleTrajectoryInside(particleTrajectoryList, velocityX);

            double avgTrajectory = particleTrajectoryList.stream()
                    .mapToDouble((Double::doubleValue))
                    .sum() / particleTrajectoryList.size();

            double std = Math.sqrt(particleTrajectoryList.stream()
                    .map(val -> Math.pow(val - avgTrajectory, 2))
                    .reduce(Double::sum).get() / particleTrajectoryList.size());
            trajectoryResults.put(velocityX, new Pair(avgTrajectory, std));
            }
            FileWriter.printParticleLengthTrajectory(trajectoryResults);
            FileWriter.printEndStateByV0(endStateResults);
        }
}

