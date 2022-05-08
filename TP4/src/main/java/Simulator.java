import integrator.Beeman;
import integrator.GearPredictorCorrector;
import integrator.VelocityVerlet;
import model.Particle;
import system.oscilator.DampedOscillator;

import java.io.IOException;

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
    }
}
