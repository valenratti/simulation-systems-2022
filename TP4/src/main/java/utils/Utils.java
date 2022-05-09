package utils;

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
}
