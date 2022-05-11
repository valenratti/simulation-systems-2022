import integrator.Beeman;
import integrator.GearPredictorCorrector;
import integrator.Verlet;
import model.Particle;
import system.oscilator.DampedOscillator;
import system.particlepropagation.ParticlePropagation;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<Double> dtList = Arrays.asList(1e-15, 1e-16, 1e-17);
        List<List<Double>> energyLists = new ArrayList<>();
        for(double currentDt : dtList){
            ParticlePropagation particlePropagation = new ParticlePropagation(1e-8, currentDt, 5e+4, 0, 1e-27);
            particlePropagation.simulate(false);
            energyLists.add(particlePropagation.getDeltaEnergyThroughTime());
        }

        //Calculate average...
    }

    public static void ej2_2(){
        int samplesQty = 30;
        double[] velocitiesArray = new double[]{50, 100, 150, 200, 250, 300, 350, 400, 450, 500};
        double velocityMultiplier = 1e+2;
        for(double v : velocitiesArray){
            double velocityX = v*velocityMultiplier;
            for(int i=0; i<samplesQty; i++){

            }
        }


    }
}
