package utils;

import member.Evil;

import java.util.Random;

public class RandomGenerator {
    public static final int EVIL_PRESENT_RATE = 75;
    public static final int EVIL_TYPE_NUMBER = 3;
    public static final int POSSIBILITY_UPPER_BOUND = 100;

    public static Random random = new Random();

    public static Evil generateEvilInCave() {
        // 75% chance to generate an evil, or 25% chance to generate nothing.
        if (random.nextInt(POSSIBILITY_UPPER_BOUND) + 1 <= EVIL_PRESENT_RATE) {
            // 3 evil type is same possibility.
            int type = random.nextInt(EVIL_TYPE_NUMBER);
            return new Evil(type);
        } else {
            return null;
        }
    }

    public static boolean randomBoolean(int winChance) {
        return random.nextInt(POSSIBILITY_UPPER_BOUND) + 1 <= winChance;
    }
}
