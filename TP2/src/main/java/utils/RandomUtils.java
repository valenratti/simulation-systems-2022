package utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static int rand(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
