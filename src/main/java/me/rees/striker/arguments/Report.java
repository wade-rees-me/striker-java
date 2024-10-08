package me.rees.striker.arguments;

import java.time.Instant;

public class Report {
	//
    private long totalRounds = 0;
    private long totalHands = 0;
    private long totalBet = 0;
    private long totalWon = 0;
    private Instant start;
    private Instant end;
    private long duration = 0;

    // Constructor
    public Report() {
        this.start = Instant.now();
        this.end = Instant.now();
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
    public void setTotalBet(long totalBet) {
        this.totalBet = totalBet;
    }

	//
    public long getTotalWon() {
        return totalWon;
    }

	//
    public void setTotalWon(long totalWon) {
        this.totalWon = totalWon;
    }

	//
    public Instant getStart() {
        return start;
    }

	//
    public void setStart(Instant start) {
        this.start = start;
    }

	//
    public Instant getEnd() {
        return end;
    }

	//
    public void setEnd(Instant end) {
        this.end = end;
    }

	//
    public long getDuration() {
        return duration;
    }

	//
    public void setDuration(long duration) {
        this.duration = duration;
    }

    // Example: Calculate the duration between start and end time
    public void calculateDuration() {
        this.duration = java.time.Duration.between(start, end).toSeconds();
    }

/*
    // Main method for testing
    public static void main(String[] args) {
        Report report = new Report();
        report.setTotalRounds(100);
        report.setTotalHands(5000);
        report.setTotalBet(100000);
        report.setTotalWon(95000);

        report.setStart(Instant.now());
        // Simulating the report duration (e.g., some gameplay or simulation activity)
        try {
            Thread.sleep(5000);  // Simulating delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        report.setEnd(Instant.now());

        report.calculateDuration();

        System.out.println("Total Rounds: " + report.getTotalRounds());
        System.out.println("Total Hands: " + report.getTotalHands());
        System.out.println("Total Bet: " + report.getTotalBet());
        System.out.println("Total Won: " + report.getTotalWon());
        System.out.println("Duration (seconds): " + report.getDuration());
    }
*/
}

