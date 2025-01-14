package me.rees.striker.arguments;

import java.time.Instant;

public class Report {
	//
	private long totalRounds = 0;
	private long totalHands = 0;
	private long totalBet = 0;
	private long totalWon = 0;
	private long totalBlackjacks = 0;
	private long totalDoubles = 0;
	private long totalSplits = 0;
	private long totalWins = 0;
	private long totalLoses = 0;
	private long totalPushes = 0;
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
}

