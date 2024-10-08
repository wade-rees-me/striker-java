package me.rees.striker;

import me.rees.striker.arguments.Arguments;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.simulator.Simulator;
import me.rees.striker.table.Rules;
import me.rees.striker.logger.Logger;
import me.rees.striker.constants.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

//
public class Main {
	//
    public static void main(String[] args) {
        String name = generateName();
        Arguments arguments = new Arguments();
        arguments.parseArguments(args);

        Logger logger = new Logger(name, arguments.getHands() < 1000);
        Locale locale = Locale.getDefault();  // Set the global locale to the default system locale
        System.out.printf(locale, "");

        Rules rules = new Rules();
        rules.rulesLoadTable(arguments.getDecks());
        Parameters params = new Parameters(name, arguments.getDecks(), arguments.getStrategy(), arguments.getNumberOfDecks(), arguments.getHands(), rules, logger);

        logger.simulation(String.format("Start: %s\n\n", Constants.STRIKER_WHO_AM_I));
        logger.simulation("  -- arguments -------------------------------------------------------------------\n");
        params.print();
        rules.print(logger);
        logger.simulation("  --------------------------------------------------------------------------------\n\n");

		try {
        	Simulator sim = new Simulator(params);
        	sim.simulatorRunOnce();
		}
		catch(Exception ex) {
		}

        logger.simulation(String.format("End: %s\n\n", Constants.STRIKER_WHO_AM_I));
    }

	//
    public static String generateName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss");
        return String.format("%s_%s", Constants.STRIKER_WHO_AM_I, now.format(formatter));
    }
}

