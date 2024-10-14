package me.rees.striker;

import me.rees.striker.arguments.Arguments;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.simulator.Simulator;
import me.rees.striker.table.Rules;
import me.rees.striker.constants.Constants;

//
public class Main {
	//
    public static void main(String[] args) {
		try {
        	Arguments arguments = new Arguments(args);
        	Parameters parameters = new Parameters(arguments.getDecks(), arguments.getStrategy(), arguments.getNumberOfDecks(), arguments.getHands());
        	Rules rules = new Rules(arguments.getDecks());
        	Simulator simulator = new Simulator(parameters, rules);

        	System.out.println(String.format("Start: %s\n", Constants.STRIKER_WHO_AM_I));
        	System.out.println("  -- arguments -------------------------------------------------------------------");
        	parameters.print();
        	rules.print();
        	System.out.println("  --------------------------------------------------------------------------------\n");

        	simulator.simulatorRunOnce();
        	System.out.println(String.format("End: %s\n\n", Constants.STRIKER_WHO_AM_I));
		}
		catch(Exception ex) {
		}
    }
}

