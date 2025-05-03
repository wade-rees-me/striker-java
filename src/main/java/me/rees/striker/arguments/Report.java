package me.rees.striker.arguments;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import me.rees.striker.constants.Constants;

//
public class Report {
  private String name = "";
  private String version = "";
  private String simulator = "";
  private String playbook = "";
  private String strategy = "";
  private String decks = "";
  private String epoch = "";

  //
  private long totalRounds = 0;
  private long totalHands = 0;
  private long totalBet = 0;
  private long totalWon = 0;
  private long totalBlackjacks = 0;
  private long totalDoubles = 0;
  private long totalSplits = 0;
  private long totalSplitsAce = 0;
  private long totalWins = 0;
  private long totalLoses = 0;
  private long totalPushes = 0;
  private long totalThreads = 1;
  private long start = 0;
  private long end = 0;
  private long duration = 0;
  private double advantage = 0.0;
  private double perBillion = 0.0;

  //
  public void init(Parameters parameters) {
    Instant now = Instant.now();
    this.start = now.getEpochSecond();

    this.name = new String(parameters.getName());
    this.version = new String(Constants.STRIKER_VERSION);
    this.simulator = new String(parameters.getSimulator());
    this.playbook = new String(parameters.getPlaybook());
    this.strategy = new String(parameters.getStrategy());
    this.decks = new String(parameters.getDecks());
    this.epoch = new String(parameters.getEpoch());
  }

  //
  public void merge(Report b) {
    totalRounds += b.totalRounds;
    totalHands += b.totalHands;
    totalBet += b.totalBet;
    totalWon += b.totalWon;
    totalBlackjacks += b.totalBlackjacks;
    totalDoubles += b.totalDoubles;
    totalSplits += b.totalSplits;
    totalSplitsAce += b.totalSplitsAce;
    totalWins += b.totalWins;
    totalLoses += b.totalLoses;
    totalPushes += b.totalPushes;
  }

  //
  public void finish() {
    Instant now = Instant.now();
    this.end = now.getEpochSecond();
    this.duration = this.end - this.start;
    this.advantage = ((double) totalWon / totalBet) * 100;
    this.perBillion = ((double) duration * (double) Constants.BILLION / (double) totalHands);
  }

  // Print results after simulation
  public void print() throws IOException {
    System.out.println(String.format("    %-26s: %,17d", "Number of hands", this.totalHands));
    System.out.println(String.format("    %-26s: %,17d", "Number of rounds", this.totalRounds));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f average bet per hand",
            "Total bet", this.totalBet, (double) this.totalBet / this.totalHands));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f average won per hand",
            "Total won", this.totalWon, (double) this.totalWon / this.totalHands));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f %% of total hands",
            "Total blackjacks",
            this.totalBlackjacks,
            (double) this.totalBlackjacks / this.totalHands * 100.0));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f %% of total hands",
            "Total doubles",
            this.totalDoubles,
            (double) this.totalDoubles / this.totalHands * 100.0));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f %% of total hands",
            "Total splits", this.totalSplits, (double) this.totalSplits / this.totalHands * 100.0));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f %% of total hands",
            "Total splits - Aces", this.totalSplitsAce, (double) this.totalSplitsAce / this.totalHands * 100.0));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f %% of total hands",
            "Total wins", this.totalWins, (double) this.totalWins / this.totalHands * 100.0));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f %% of total hands",
            "Total pushes", this.totalPushes, (double) this.totalPushes / this.totalHands * 100.0));
    System.out.println(
        String.format(
            "    %-26s: %,17d %+08.3f %% of total hands",
            "Total loses", this.totalLoses, (double) this.totalLoses / this.totalHands * 100.0));
    System.out.println(String.format("    %-26s: %,17d seconds", "Total time", this.getDuration()));
    System.out.println(
        String.format("    %-26s: %,17d threads", "Total threads", this.totalThreads));
    System.out.println(
        String.format(
            "    %-26s: %17.0f seconds per %,d hands",
            "Average time", this.perBillion, Constants.BILLION));
    System.out.println(
        String.format("    %-26s: %17s %,+08.3f %%", "Player advantage", "", this.advantage));
  }

  // Insert simulation results into the database
  public void insert() {
    if (this.totalHands < Constants.NUMBER_OF_HANDS_DATABASE) {
      System.out.println(
          String.format(
              "    Error: Not enough hands played (%d). Minimum required is %d",
              this.totalHands, Constants.NUMBER_OF_HANDS_DATABASE));
      return;
    }
    try {
      URL url =
          new URL(
              String.format(
                  "http://%s/%s/%s/%s", Constants.getSimulationsUrl(), simulator, playbook, name));
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set request method and headers
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);

      // Send the request
      JsonObject json = toJsonObject();
      try (OutputStream os = connection.getOutputStream()) {
        byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
      }

      // Get the response
      int responseCode = connection.getResponseCode();
      if (responseCode != HttpURLConnection.HTTP_OK) {
        System.out.println(
            String.format("    HTTP request failed with response code: " + responseCode));
        return;
      }
      System.out.println("    Insert successful");

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(String.format("    Failed to insert simulation data"));
    }
  }

  public JsonObject toJsonObject() {
    JsonObject json = new JsonObject();

    json.addProperty("guid", name);
    json.addProperty("version", version);
    json.addProperty("simulator", simulator);
    json.addProperty("threads", totalThreads);
    json.addProperty("playbook", playbook);
    json.addProperty("decks", decks);
    json.addProperty("playbook", playbook);
    json.addProperty("decks", decks);
    json.addProperty("strategy", strategy);
    json.addProperty("rounds", totalRounds);
    json.addProperty("hands", totalHands);
    json.addProperty("total_bet", totalBet);
    json.addProperty("total_won", totalWon);
    json.addProperty("total_blackjacks", totalBlackjacks);
    json.addProperty("total_doubles", totalDoubles);
    json.addProperty("total_splits", totalSplits);
    json.addProperty("total_splits_ace", totalSplitsAce);
    json.addProperty("total_wins", totalWins);
    json.addProperty("total_loses", totalLoses);
    json.addProperty("total_pushes", totalPushes);
    json.addProperty("advantage", advantage);
    json.addProperty("epoch", epoch);
    json.addProperty("start", start);
    json.addProperty("end", end);
    json.addProperty("duration", duration);
    json.addProperty("per_billion", perBillion);

    return json;
  }

  // Getters and Setters for each field
  public long getTotalRounds() {
    return totalRounds;
  }

  //
  public void setTotalRounds(long totalRounds) {
    this.totalRounds = totalRounds;
  }

  //
  public long getTotalHands() {
    return totalHands;
  }

  //
  public void setTotalHands(long totalHands) {
    this.totalHands = totalHands;
  }

  //
  public long getTotalBet() {
    return totalBet;
  }

  //
  public void addTotalBet(long totalBet) {
    this.totalBet += totalBet;
  }

  //
  public void setTotalBet(long totalBet) {
    this.totalBet = totalBet;
  }

  //
  public long getTotalWon() {
    return totalWon;
  }

  //
  public void addTotalWon(long totalWon) {
    this.totalWon += totalWon;
  }

  //
  public void setTotalWon(long totalWon) {
    this.totalWon = totalWon;
  }

  //
  public long getTotalBlackjacks() {
    return totalBlackjacks;
  }

  //
  public void addTotalBlackjacks() {
    this.totalBlackjacks++;
  }

  //
  public void setTotalBlackjacks(long totalBlackjacks) {
    this.totalBlackjacks = totalBlackjacks;
  }

  //
  public long getTotalDoubles() {
    return totalDoubles;
  }

  //
  public void addTotalDoubles() {
    this.totalDoubles++;
  }

  //
  public void setTotalDoubles(long totalDoubles) {
    this.totalDoubles = totalDoubles;
  }

  //
  public long getTotalSplits() {
    return totalSplits;
  }

  //
  public void addTotalSplits() {
    this.totalSplits++;
  }

  //
  public void setTotalSplits(long totalSplits) {
    this.totalSplits = totalSplits;
  }

  //
  public long getTotalSplitsAce() {
    return totalSplitsAce;
  }

  //
  public void addTotalSplitsAce() {
    this.totalSplitsAce++;
  }

  //
  public void setTotalSplitsAce(long totalSplitsAce) {
    this.totalSplitsAce = totalSplitsAce;
  }

  //
  public long getTotalWins() {
    return totalWins;
  }

  //
  public void addTotalWins() {
    this.totalWins++;
  }

  //
  public void setTotalWins(long totalWins) {
    this.totalWins = totalWins;
  }

  //
  public long getTotalPushes() {
    return totalPushes;
  }

  //
  public void addTotalPushes() {
    this.totalPushes++;
  }

  //
  public void setTotalPushes(long totalPushes) {
    this.totalPushes = totalPushes;
  }

  //
  public long getTotalLoses() {
    return totalLoses;
  }

  //
  public void addTotalLoses() {
    this.totalLoses++;
  }

  //
  public void setTotalLoses(long totalLoses) {
    this.totalLoses = totalLoses;
  }

  //
  public long getStart() {
    return start;
  }

  //
  public void setStart(long start) {
    this.start = start;
  }

  //
  public long getEnd() {
    return end;
  }

  //
  public void setEnd(long end) {
    this.end = end;
  }

  //
  public long getDuration() {
    return duration;
  }
}
