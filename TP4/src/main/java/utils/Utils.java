package utils;

import model.Particle;
import system.oscilator.System;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static int rand(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static double randDouble(double min, double max){
        return ThreadLocalRandom.current().nextDouble(min,max);
    }

    public static long factorial(int n) {
        long fact = 1;

        for (int i = 2; i <= n; i++)
            fact = fact * i;

        return fact;
    }

    public static double squaredError(double x1, double x2) {
        return Math.pow(x1 - x2, 2);
    }

    public static String fromDoubleListToCsvLine(Double firstColumn, List<Double> list, String format) {
        StringBuilder ans = new StringBuilder();

        if(firstColumn != null)
            ans.append(format == null ? firstColumn : String.format("%.1e", firstColumn)).append(",");

        if(format != null)
            for(Double s : list)
                ans.append(String.format(format, s)).append(",");
        else
            for(Double s : list)
                ans.append(s).append(",");

        return ans.deleteCharAt(ans.length()-1).toString(); // removes extra comma before returning
    }

    public static Particle euler(Particle particle, double dt, System system) {
        final Pair f0 = system.force(particle);
        final double m = particle.getMass();
        final double ax0 = f0.getX() / m, ay0 = f0.getY();
        final double vx0 = particle.getVx(), vy0 = particle.getVy();

        final double rx = particle.getX() + dt * vx0 + dt * dt / 2 * ax0;
        final double ry = particle.getY() + dt * vy0 + dt * dt / 2 * ay0;

        final double vx = vx0 + dt * ax0;
        final double vy = vy0 + dt * ay0;

        return new Particle(rx, ry, vx, vy, m, particle.getCharge(), true);
    }
}
