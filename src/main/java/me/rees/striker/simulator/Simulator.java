package me.rees.striker.simulator;

import me.rees.striker.table.Rules;
import me.rees.striker.table.Strategy;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.arguments.Arguments;
import me.rees.striker.arguments.Report;
import me.rees.striker.simulator.Simulator;
import me.rees.striker.constants.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.DecimalFormat;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Simulator {
	//
	private Parameters parameters;
	private Rules rules;
	private Table table;
	private Report report;

	//
	public Simulator(Parameters parameters, Rules rules, Strategy strategy) {
		this.parameters = parameters;
		this.rules = rules;
		this.table = new Table(parameters, rules, strategy);
		this.report = new Report();
	}

	// The main simulator process
	public void simulatorRunOnce() throws IOException {
		Simulation dbTable = new Simulation();

		System.out.println(String.format("  Start: simulation %s", parameters.getName()));
		simulatorRunSimulation();
		System.out.println(String.format("  End: simulation"));

		// Populate Simulation
		dbTable.setPlaybook(parameters.getPlaybook());
		dbTable.setName(parameters.getName());
		dbTable.setSimulator(Constants.STRIKER_WHO_AM_I);
		dbTable.setSummary("no");
		dbTable.setSimulations("1");
		dbTable.setParameters(parameters.serialize());
		dbTable.setRules(rules.serialize());

		// Format rounds, hands, and other values
		dbTable.setRounds(Long.toString(report.getTotalRounds()));
		dbTable.setHands(Long.toString(report.getTotalHands()));
		dbTable.setTotalBet(Long.toString(report.getTotalBet()));
		dbTable.setTotalWon(Long.toString(report.getTotalWon()));
		dbTable.setTotalTime(Long.toString(report.getDuration()));

		DecimalFormat df = new DecimalFormat("00.00");
		dbTable.setAverageTime(df.format((float) report.getDuration() * 1000000 / report.getTotalHands()) + " seconds");
		dbTable.setAdvantage(String.format("%+04.3f %%", ((double)report.getTotalWon() / report.getTotalBet()) * 100));

		// Print results
		printReport(dbTable);

		if (report.getTotalHands() >= Constants.DATABASE_NUMBER_OF_HANDS) {
			simulatorInsert(dbTable, parameters.getPlaybook());
		}
	}

	// Run the simulation
	private void simulatorRunSimulation() throws IOException {
		System.out.println("	Start: " + parameters.getStrategy() + " table session");
		table.session(parameters.getStrategy().equals("mimic"));
		System.out.println("	End: table session");

		report.setTotalBet(table.getPlayer().getReport().getTotalBet());
		report.setTotalWon(table.getPlayer().getReport().getTotalWon());
		report.setTotalBlackjacks(table.getPlayer().getReport().getTotalBlackjacks());
		report.setTotalDoubles(table.getPlayer().getReport().getTotalDoubles());
		report.setTotalSplits(table.getPlayer().getReport().getTotalSplits());
		report.setTotalWins(table.getPlayer().getReport().getTotalWins());
		report.setTotalPushes(table.getPlayer().getReport().getTotalPushes());
		report.setTotalLoses(table.getPlayer().getReport().getTotalLoses());
		report.setTotalRounds(table.getReport().getTotalRounds());
		report.setTotalHands(table.getReport().getTotalHands());
		report.setDuration(table.getReport().getDuration());
	}

	// Insert simulation results into the database
	private void simulatorInsert(Simulation sdt, String playbook) {
		System.out.println("\n  -- insert ----------------------------------------------------------------------");
		try {
			URL url = new URL(String.format("http://%s/%s/%s/%s", Constants.getSimulationUrl(), sdt.getSimulator(), playbook, sdt.getName()));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// Set request method and headers
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			// Create JSON object using Gson
			JsonObject json = new JsonObject();
			json.addProperty("playbook", playbook);
			json.addProperty("name", sdt.getName());
			json.addProperty("simulator", sdt.getSimulator());
			json.addProperty("summary", "no");
			json.addProperty("simulations", "1");
			json.addProperty("rounds", new String(sdt.getRounds()));
			json.addProperty("hands", new String(sdt.getHands()));
			json.addProperty("total_bet", new String(sdt.getTotalBet()));
			json.addProperty("total_won", new String(sdt.getTotalWon()));
			json.addProperty("advantage", new String(sdt.getAdvantage()));
			json.addProperty("total_time", new String(sdt.getTotalTime()));
			json.addProperty("average_time", new String(sdt.getAverageTime()));
			json.addProperty("parameters", sdt.getParameters());
			json.addProperty("rules", sdt.getRules());
			json.addProperty("payload", "n/a");

			// Send the request
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			// Get the response
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("HTTP request failed with response code: " + responseCode);
			}

			System.out.println("Insert successful");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to insert simulation data");
		}
		System.out.println("\n  --------------------------------------------------------------------------------");
	}

	// Print results after simulation
	private void printReport(Simulation dbTable) throws IOException {
		System.out.println("\n  -- results ---------------------------------------------------------------------");
		System.out.println(String.format("    %-24s: %d", "Number of hands", this.report.getTotalHands()));
		System.out.println(String.format("    %-24s: %d", "Number of rounds", this.report.getTotalRounds()));
		System.out.println(String.format("    %-24s: %d %+04.3f average bet per hand", "Total bet", this.report.getTotalBet(), (double) this.report.getTotalBet() / this.report.getTotalHands()));
		System.out.println(String.format("    %-24s: %d %+04.3f average won per hand", "Total won", this.report.getTotalWon(), (double) this.report.getTotalWon() / this.report.getTotalHands()));
		System.out.println(String.format("    %-24s: %d %+04.3f percent of total hands", "Total blackjacks", this.report.getTotalBlackjacks(), (double) this.report.getTotalBlackjacks() / this.report.getTotalHands() * 100.0));
		System.out.println(String.format("    %-24s: %d %+04.3f percent of total hands", "Total doubles", this.report.getTotalDoubles(), (double) this.report.getTotalDoubles() / this.report.getTotalHands() * 100.0));
		System.out.println(String.format("    %-24s: %d %+04.3f percent of total hands", "Total splits", this.report.getTotalSplits(), (double) this.report.getTotalSplits() / this.report.getTotalHands() * 100.0));
		System.out.println(String.format("    %-24s: %d %+04.3f percent of total hands", "Total wins", this.report.getTotalWins(), (double) this.report.getTotalWins() / this.report.getTotalHands() * 100.0));
		System.out.println(String.format("    %-24s: %d %+04.3f percent of total hands", "Total pushes", this.report.getTotalPushes(), (double) this.report.getTotalPushes() / this.report.getTotalHands() * 100.0));
		System.out.println(String.format("    %-24s: %d %+04.3f percent of total hands", "Total loses", this.report.getTotalLoses(), (double) this.report.getTotalLoses() / this.report.getTotalHands() * 100.0));
		System.out.println(String.format("    %-24s: %d seconds", "Total time", this.report.getDuration()));
		System.out.println(String.format("    %-24s: %s per 1,000,000 hands", "Average time", dbTable.getAverageTime()));
		System.out.println(String.format("    %-24s: %s", "Player advantage", dbTable.getAdvantage()));
		System.out.println("  --------------------------------------------------------------------------------\n");
	}
}

