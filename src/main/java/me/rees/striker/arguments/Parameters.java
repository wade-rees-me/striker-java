package me.rees.striker.arguments;

import me.rees.striker.table.Rules;
import me.rees.striker.logger.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;


public class Parameters {
	//
    private Rules rules;
    private Logger logger;

    private String name;
    private String playbook;
    private String processor;
    private String timestamp;
    private String decks;
    private String strategy;
    private int numberOfDecks;
    private long numberOfHands;

    // Constructor to initialize parameters
    public Parameters(String name, String decks, String strategy, int numberOfDecks, long numberOfHands, Rules rules, Logger logger) {
        this.name = name;
        this.decks = decks;
        this.strategy = strategy;
        this.numberOfDecks = numberOfDecks;
        this.numberOfHands = numberOfHands;
        this.rules = rules;
        this.logger = logger;
        this.playbook = decks + "-" + strategy;
        this.processor = "striker-java";  // Replace STRIKER_WHO_AM_I
        this.timestamp = getCurrentTime();
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
    public Logger getLogger() {
		return this.logger;
    }

	//
    public Rules getRules() {
		return this.rules;
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
        logger.simulation(String.format("    %-24s: %s\n", "Name", name));
        logger.simulation(String.format("    %-24s: %s\n", "Playbook", playbook));
        logger.simulation(String.format("    %-24s: %s\n", "Processor", processor));
        logger.simulation(String.format("    %-24s: %s\n", "Version", "v01.02.02"));  // Replace STRIKER_VERSION
        logger.simulation(String.format("    %-24s: %,d\n", "Number of hands", numberOfHands));
        logger.simulation(String.format("    %-24s: %s\n", "Timestamp", timestamp));
    }

    // Function to get current timestamp in a formatted string
    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(formatter);
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
        json.append(String.format("\"hit_soft_17\": \"%s\",\n", rules.isHitSoft17() ? "true" : "false"));
        json.append(String.format("\"surrender\": \"%s\",\n", rules.isSurrender() ? "true" : "false"));
        json.append(String.format("\"double_any_two_cards\": \"%s\",\n", rules.isDoubleAnyTwoCards() ? "true" : "false"));
        json.append(String.format("\"double_after_split\": \"%s\",\n", rules.isDoubleAfterSplit() ? "true" : "false"));
        json.append(String.format("\"resplit_aces\": \"%s\",\n", rules.isResplitAces() ? "true" : "false"));
        json.append(String.format("\"hit_split_aces\": \"%s\",\n", rules.isHitSplitAces() ? "true" : "false"));
        json.append(String.format("\"blackjack_bets\": %d,\n", rules.getBlackjackBets()));
        json.append(String.format("\"blackjack_pays\": %d,\n", rules.getBlackjackPays()));
        json.append(String.format("\"penetration\": %.2f\n", rules.getPenetration()));
        json.append("}");
        return json.toString();
    }
}

