import integrator.Beeman;
import integrator.GearPredictorCorrector;
import integrator.Verlet;
import model.Particle;
import system.oscilator.DampedOscillator;
import system.particlepropagation.ParticlePropagation;
import system.particlepropagation.State;
import utils.FileWriter;
import utils.Utils;

import java.io.IOException;
import java.util.*;

public class Simulator {
    public static void simulateSystem1(double m, int k, int gamma, double A, double tf, double r0, double v0, double dt, double dt2) {
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

        System.out.println("Initial conditions and parameters:\n" +
                "dt = " + dt + " seconds\n" +
                "dt2 = " + dt2 + " seconds (" + logStep + " times dt)\n" +
                "cut time = " + tf);

        // TODO: log initial parameters ?
        // TODO: log initial positions
        System.out.println("t,beeman,verlet,gpc,analytical_solution");
        System.out.println( Utils.fromDoubleListToCsvLine( Arrays.asList( time, p1.getX(), p2.getX(), p3.getX(), p4.getX() ) ) );

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
                // TODO: log time and positions
                System.out.println( Utils.fromDoubleListToCsvLine( Arrays.asList( time, p1.getX(), p2.getX(), p3.getX(), p4.getX() ) ) );
                dampedOscillator.analyticalSolution(p4, time);
                aux = 0;
            }
        }

        final double n = time / dt;     // in fact it's an int, but it's not significant for the mse calculus
        final double mse1 = e1 / n, mse2 = e2 / n, mse3 = e3 / n;   // mean squared errors

        // TODO: log mean squared errors
        System.out.println("\nMean squared errors:" + "\nBeeman: " + mse1 + "\nVerlet: " + mse2 + "\nGPC: " + mse3);
    }

    public static void simulateSystem2() throws IOException {
        ParticlePropagation particlePropagation = new ParticlePropagation(1e-8, 1e-13, 1e+3, 0, 1e-27);
        particlePropagation.simulate(true);
    }

    public static void ej2_1() throws IOException {
        List<Double> dtList = Arrays.asList(1e-13, 1e-14, 1e-15, 1e-16, 1e-17);
//        double initialY = Utils.randDouble((l/2) - d, (l/2) + d);
        double d = 1e-8;
        double l = d * 15;
        double initialY = (l / 2) - d;
        double yVariation = 2 * d / 5;
        List<Double> initialYList = Arrays.asList(initialY, initialY + yVariation, initialY + 2 * yVariation, initialY
                + 3 * yVariation, initialY + 4 * yVariation);
        for (double currentDt : dtList) {
            List<List<Double>> energyLists = new ArrayList<>();
            Map<Double, Double> dtEnergyMap = new HashMap<>();
            for (double y : initialYList) {
                ParticlePropagation particlePropagation = new ParticlePropagation(d, currentDt, 5e+4, 0, 1e-27, y);
                particlePropagation.simulate(false);
                energyLists.add(particlePropagation.getDeltaEnergyThroughTime());
            }

            int maxSize = energyLists.stream().max(Comparator.comparing(List::size)).get().size();
            for (int i = 1; i < maxSize + 1; i++) {
                int qty = 0;
                double value = 0;
                for (List<Double> currentList : energyLists) {
                    try {
                        value += currentList.get(i - 1);
                        qty++;
                    } catch (IndexOutOfBoundsException e) {
                        //do nothing.
                    }
                }
                dtEnergyMap.put(i * currentDt, value / qty);
            }
            FileWriter.printEnergyDiffVsTime(dtEnergyMap, currentDt);
        }

        //Calculate average...
    }

    public static void ej2_2() throws IOException {
        int samplesQty = 30;
        double[] velocitiesArray = new double[]{50, 150, 250, 350, 450};
        double velocityMultiplier = 1e+2;
        double d = 1e-8;
        double dt = 1e-15;
        Map<Double, Map<State,Integer>> endStateResults = new HashMap<>();
        for (double v : velocitiesArray) {
            Map<State, Integer> endStateCount = new HashMap<>();
            List<List<Double>> particleTrajectoryList = new ArrayList<>();
            double velocityX = v * velocityMultiplier;
            for (int i = 0; i < samplesQty; i++) {
                ParticlePropagation particlePropagation = new ParticlePropagation(d, dt, velocityX, 0.0, 1e-27);
                //Only save last sample
                State endState = particlePropagation.simulate(i == samplesQty - 1);
                endStateCount.put(endState, endStateCount.get(endState) + 1);
                particleTrajectoryList.add(particlePropagation.calculateParticleTrajectory());
            }
            endStateResults.put(velocityX, endStateCount);

            List<Double> avgLengths = new LinkedList<>();
            List<Double> stdLengths = new LinkedList<>();
            int maxSize = particleTrajectoryList.stream()
                    .map(List::size)
                    .max(Comparator.naturalOrder())
                    .get();
            for (int i = 0; i < maxSize; i++) {
                List<Double> valuesAtI = new LinkedList<>();
                for (List<Double> lengths : particleTrajectoryList) {
                    if (lengths.size() > i) {
                        valuesAtI.add(lengths.get(i));
                    }
                }
                Double avg = valuesAtI.stream()
                        .reduce((x, y) -> x + y)
                        .get() / valuesAtI.size();

                Double std = Math.sqrt(valuesAtI.stream()
                        .map(val -> Math.pow(val - avg, 2))
                        .reduce((v1, v2) -> v1 + v2)
                        .get() / valuesAtI.size());

                avgLengths.add(avg);
                stdLengths.add(std);
            }
            FileWriter.printParticleLengthTrajectory(avgLengths, stdLengths, dt, velocityX);
            FileWriter.reset();
        }
    }
}
