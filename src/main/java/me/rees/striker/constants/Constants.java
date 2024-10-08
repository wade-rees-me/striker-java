package me.rees.striker.constants;

public class Constants {

    // General constants
    public static final String STRIKER_WHO_AM_I = "striker-java";
    public static final String STRIKER_VERSION = "v01.02.02";
    public static final String TIME_LAYOUT = "yyyy-MM-dd HH:mm:ss Z";

    // Simulation constants
    public static final long MAXIMUM_NUMBER_OF_HANDS = 1000000000L;
    public static final long MINIMUM_NUMBER_OF_HANDS = 100L;
    public static final long DEFAULT_NUMBER_OF_HANDS = 1000000L;
    public static final long DATABASE_NUMBER_OF_HANDS = 1000000L;

    // Betting constants
    public static final int MAXIMUM_BET = 80;
    public static final int MINIMUM_BET = 2;

    // Methods to get environment variables
    public static String getRulesUrl() {
        return System.getenv("STRIKER_URL_RULES");
    }

    public static String getStrategyUrl() {
        return System.getenv("STRIKER_URL_ACE");
    }

    public static String getStrategyMlbUrl() {
        return System.getenv("STRIKER_URL_MLB");
    }

    public static String getSimulationUrl() {
        return System.getenv("STRIKER_URL_SIMULATION");
    }

    public static String getSimulationDirectory() {
        return System.getenv("STRIKER_SIMULATIONS");
    }
}

