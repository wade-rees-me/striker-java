package me.rees.striker.arguments;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import me.rees.striker.constants.Constants;

public class Parameters {
  //
  private String name;
  private String playbook;
  private String processor;
  private String epoch;
  private String decks;
  private String strategy;
  private int numberOfDecks;
  private long numberOfHands;
  private long numberOfThreads;

  // Constructor to initialize parameters
  public Parameters(String decks, String strategy, int numberOfDecks, long numberOfHands) {
    this.decks = decks;
    this.strategy = strategy;
    this.numberOfDecks = numberOfDecks;
    this.numberOfHands = numberOfHands;
    this.numberOfThreads = 1;
    this.playbook = decks + "-" + strategy;
    this.processor = Constants.STRIKER_WHO_AM_I;
    this.epoch = getCurrentTime();
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
  public String getDecks() {
    return this.decks;
  }

  //
  public String getSimulator() {
    return this.processor;
  }

  //
  public String getEpoch() {
    return this.epoch;
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
    System.out.println(String.format("    %-26s: %s", "Processor", processor));
    System.out.println(String.format("    %-26s: %,17d", "Threads", numberOfThreads));
    System.out.println(String.format("    %-26s: %s", "Name", name));
    System.out.println(String.format("    %-26s: %s", "Version", Constants.STRIKER_VERSION));
    System.out.println(String.format("    %-26s: %s", "Playbook", playbook));
    System.out.println(String.format("    %-26s: %s", "Decks", decks));
    System.out.println(String.format("    %-26s: %s", "Strategy", strategy));
    System.out.println(String.format("    %-26s: %,17d", "Number of hands", numberOfHands));
    System.out.println(String.format("    %-26s: %,17d", "Thread's share of hands", numberOfHands));
    System.out.println(String.format("    %-26s: %s", "Epoch", epoch));
  }

  // Function to get current epoch in a formatted string
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
