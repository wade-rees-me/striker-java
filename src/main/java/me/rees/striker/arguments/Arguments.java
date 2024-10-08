package me.rees.striker.arguments;

import me.rees.striker.constants.Constants;

import java.util.HashMap;
import java.util.Map;

public class Arguments {
	//
    private long numberOfHands = Constants.DEFAULT_NUMBER_OF_HANDS;
    private boolean mimicFlag = false;
    private boolean basicFlag = false;
    private boolean linearFlag = false;
    private boolean polynomialFlag = false;
    private boolean highLowFlag = false;
    private boolean wongFlag = false;
    private boolean strikerFlag = false;
    private boolean singleDeckFlag = false;
    private boolean doubleDeckFlag = false;
    private boolean sixShoeFlag = false;

    // Parse command-line flags for simulation
    public void parseArguments(String[] args) {
        Map<String, Runnable> argHandlers = new HashMap<>();
        argHandlers.put("-M", () -> mimicFlag = true);
        argHandlers.put("--mimic", () -> mimicFlag = true);
        argHandlers.put("-B", () -> basicFlag = true);
        argHandlers.put("--basic", () -> basicFlag = true);
        argHandlers.put("-L", () -> linearFlag = true);
        argHandlers.put("--linear", () -> linearFlag = true);
        argHandlers.put("-P", () -> polynomialFlag = true);
        argHandlers.put("--polynomial", () -> polynomialFlag = true);
        argHandlers.put("-H", () -> highLowFlag = true);
        argHandlers.put("--high-low", () -> highLowFlag = true);
        argHandlers.put("-W", () -> wongFlag = true);
        argHandlers.put("--wong", () -> wongFlag = true);
        argHandlers.put("-S", () -> strikerFlag = true);
        argHandlers.put("--striker", () -> strikerFlag = true);
        argHandlers.put("-1", () -> singleDeckFlag = true);
        argHandlers.put("--single-deck", () -> singleDeckFlag = true);
        argHandlers.put("-2", () -> doubleDeckFlag = true);
        argHandlers.put("--double-deck", () -> doubleDeckFlag = true);
        argHandlers.put("-6", () -> sixShoeFlag = true);
        argHandlers.put("--six-shoe", () -> sixShoeFlag = true);

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if ((arg.equals("-h") || arg.equals("--number-of-hands")) && i + 1 < args.length) {
                try {
                    numberOfHands = Long.parseLong(args[++i]);
                    if (numberOfHands < Constants.MINIMUM_NUMBER_OF_HANDS || numberOfHands > Constants.MAXIMUM_NUMBER_OF_HANDS) {
                        throw new IllegalArgumentException("Number of hands must be between " + Constants.MINIMUM_NUMBER_OF_HANDS + " and " + Constants.MAXIMUM_NUMBER_OF_HANDS);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number of hands");
                    System.exit(1);
                }
            } else if (argHandlers.containsKey(arg)) {
                argHandlers.get(arg).run();
            } else if (arg.equals("--help")) {
                printHelpMessage();
                System.exit(0);
            } else if (arg.equals("--version")) {
                printVersion();
                System.exit(0);
            } else {
                System.err.println("Error: Invalid argument: " + arg);
                System.exit(1);
            }
        }
    }

    // Function to get the current strategy as a string
    public String getStrategy() {
        if (mimicFlag) return "mimic";
        if (polynomialFlag) return "polynomial";
        if (linearFlag) return "linear";
        if (highLowFlag) return "high-low";
        if (wongFlag) return "wong";
        if (strikerFlag) return "striker";
        return "basic";
    }

    // Function to get the current deck type as a string
    public String getDecks() {
        if (doubleDeckFlag) return "double-deck";
        if (sixShoeFlag) return "six-shoe";
        return "single-deck";
    }

    // Function to get the number of decks
    public int getNumberOfDecks() {
        if (doubleDeckFlag) return 2;
        if (sixShoeFlag) return 6;
        return 1;
    }

    // Function to get the number of hands
    public long getHands() {
        return numberOfHands;
    }

    // Print the version of the program
    private void printVersion() {
        System.out.println(Constants.STRIKER_WHO_AM_I + ": version: " + Constants.STRIKER_VERSION);
    }

    // Print the help message for the program
    private void printHelpMessage() {
        System.out.println("Usage: strikerJava [options]\n" +
                "Options:\n" +
                "  --help                                   Show this help message\n" +
                "  --version                                Display the program version\n" +
                "  -h, --number-of-hands <number of hands>  The number of hands to play in this simulation\n" +
                "  -M, --mimic                              Use the mimic dealer player strategy\n" +
                "  -B, --basic                              Use the basic player strategy\n" +
                "  -L, --linear                             Use the liner regression player strategy\n" +
                "  -P, --polynomial                         Use the polynomial regression player strategy\n" +
                "  -H, --high-low                           Use the high low count player strategy\n" +
                "  -W, --wong                               Use the Wong count player strategy\n" +
                "  -S, --striker                            Use the Striker machine learning player strategy\n" +
                "  -1, --single-deck                        Use a single deck of cards and rules\n" +
                "  -2, --double-deck                        Use a double deck of cards and rules\n" +
                "  -6, --six-shoe                           Use a six deck shoe of cards and rules");
    }
}

