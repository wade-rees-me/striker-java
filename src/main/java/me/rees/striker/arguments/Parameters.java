package me.rees.striker.arguments;

import me.rees.striker.constants.Constants;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Parameters {
	//
	private String name;
	private String playbook;
	private String processor;
	private String timestamp;
	private String decks;
	private String strategy;
	private int numberOfDecks;
	private long numberOfHands;

	// Constructor to initialize parameters
	public Parameters(String decks, String strategy, int numberOfDecks, long numberOfHands) {
		this.decks = decks;
		this.strategy = strategy;
		this.numberOfDecks = numberOfDecks;
		this.numberOfHands = numberOfHands;
		this.playbook = decks + "-" + strategy;
		this.processor = Constants.STRIKER_WHO_AM_I;
		this.timestamp = getCurrentTime();
		this.name = generateName();
	}

	//
	public String getPlaybook() {
		return this.playbook;
	}

	//
	public String getName() {
		return this.name;
	}

	//
	public String getStrategy() {
		return this.strategy;
	}

	//
	public int getNumberOfDecks() {
		return this.numberOfDecks;
	}

	//
	public long getNumberOfHands() {
		return this.numberOfHands;
	}

	// Print method to log simulation parameters
	public void print() {
		System.out.println(String.format("    %-24s: %s", "Name", name));
		System.out.println(String.format("    %-24s: %s", "Playbook", playbook));
		System.out.println(String.format("    %-24s: %s", "Processor", processor));
		System.out.println(String.format("    %-24s: %s", "Version", Constants.STRIKER_VERSION));
		System.out.println(String.format("    %-24s: %,d", "Number of hands", numberOfHands));
		System.out.println(String.format("    %-24s: %s", "Timestamp", timestamp));
	}

	// Serialize the Parameters object to a JSON-like string
	public String serialize() {
		StringBuilder json = new StringBuilder();
		json.append("{\n");
		json.append(String.format("\"playbook\": \"%s\",\n", playbook));
		json.append(String.format("\"name\": \"%s\",\n", name));
		json.append(String.format("\"processor\": \"%s\",\n", processor));
		json.append(String.format("\"timestamp\": \"%s\",\n", timestamp));
		json.append(String.format("\"decks\": \"%s\",\n", decks));
		json.append(String.format("\"strategy\": \"%s\",\n", strategy));
		json.append(String.format("\"number_of_hands\": %d,\n", numberOfHands));
		json.append(String.format("\"number_of_decks\": %d,\n", numberOfDecks));
		json.append("}");
		return json.toString();
	}

	// Function to get current timestamp in a formatted string
	private String getCurrentTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
		return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);
	}

	//
	private static String generateName() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss");
		return String.format("%s_%s", Constants.STRIKER_WHO_AM_I, now.format(formatter));
	}
}

