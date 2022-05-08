import integrator.Beeman;
import integrator.GearPredictorCorrector;
import integrator.VelocityVerlet;
import model.Particle;
import system.oscilator.DampedOscillator;
import system.particlepropagation.ParticlePropagation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulator {
    public static void simulateSystem1(double m, int k, int gamma, double A, double tf, double r0, double v0, double dt, double dt2) {
        // time unit --> seconds

        double time = 0.0;
        int aux = 0;

        final double logStep = dt2 / dt;

        final Particle p1 = new Particle(r0, 0, v0, 0, m, 0, false);
        final Particle p2 = new Particle(r0, 0, v0, 0, m, 0, false);
        final Particle p3 = new Particle(r0, 0, v0, 0, m, 0, false);
        final Particle p4 = new Particle(r0, 0, v0, 0, m, 0, false);

        final DampedOscillator dampedOscillator = new DampedOscillator(k, gamma, A);

        final Beeman beeman = new Beeman(dt, dampedOscillator);
        final VelocityVerlet velocityVerlet = new VelocityVerlet(dt, dampedOscillator);
        final GearPredictorCorrector gearPredictorCorrector = new GearPredictorCorrector(dt, dampedOscillator);

        System.out.println("Initial conditions and parameters:\n" +
                "dt = " + dt + " seconds\n" +
                "dt2 = " + dt2 + " seconds (" + logStep + " times dt)");

        // TODO: log initial state ?

        while(time < tf) {
            dampedOscillator.analyticalSolution(p1, time);
            beeman.nextStep(p2);
            velocityVerlet.nextStep(p3);
            gearPredictorCorrector.nextStep(p4);

            // TODO: calculate errors

            time += dt; // FIXME: antes o despues de evolucionar?
            aux++;

            if(aux == logStep) {
                // TODO: log state
                aux = 0;
            }
        }
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
