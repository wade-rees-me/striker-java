package me.rees.striker.cards;

import me.rees.striker.constants.Constants;

public class Wager extends Hand {
	//
	private int amountBet;		 // The amount bet
	private int amountWon;		 // The amount won
	private int insuranceBet;	  // The insurance bet
	private int insuranceWon;	  // The insurance winnings

	// Constructor: Reset the wager to its initial state
	public Wager() {
		reset();
	}

	// Reset the wager to initial state
	@Override
	public void reset() {
		super.reset();
		amountBet = 0;
		amountWon = 0;
		insuranceBet = 0;
		insuranceWon = 0;
	}

	//
	public void setAmountBet(int bet) {
		this.amountBet = bet;
	}

	//
	public void setInsuranceBet(int bet) {
		this.insuranceBet = bet;
	}

	// Split the wager by copying the bet amount to the new hand
	public void splitHand(Wager split) {
		split.amountBet = this.amountBet;
		split.drawCard(this.splitPair());
	}

	// Place the bet (ensure it's within the minimum and maximum range)
	public void placeBet(int bet) {
		amountBet = (Math.min(Constants.MAXIMUM_BET, Math.max(Constants.MINIMUM_BET, bet)) + 1) / 2 * 2;
	}

	// Double the bet
	public void doubleBet() {
		amountBet *= 2;
	}

	// Record a blackjack win
	public void wonBlackjack(int pays, int bet) {
		amountWon = (amountBet * pays) / bet;
	}

	// Record a win
	public void won() {
		amountWon = amountBet;
	}

	// Record a loss
	public void lost() {
		amountWon = -amountBet;
	}

	// Push (tie) - no action needed
	public void push() {
		// No action needed
	}

	// Record insurance win
	public void wonInsurance() {
		insuranceWon = insuranceBet * 2;
	}

	// Record insurance loss
	public void lostInsurance() {
		insuranceWon = -insuranceBet;
	}

	//
	public int getAmountBet() {
		return amountBet;
	}

	public int getAmountWon() {
		return amountWon;
	}

	public int getInsuranceBet() {
		return insuranceBet;
	}

	public int getInsuranceWon() {
		return insuranceWon;
	}
}

