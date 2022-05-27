package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static double rand(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
