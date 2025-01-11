package me.rees.striker.cards;

public class Wager extends Hand {
	//
	private int minimumBet;
	private int maximumBet;
	private int amountBet;		 // The amount bet
	private int amountWon;		 // The amount won
	private int insuranceBet;	  // The insurance bet
	private int insuranceWon;	  // The insurance winnings

	// Constructor: Reset the wager to its initial state
	public Wager(int minimumBet, int maximumBet) {
		reset();
		this.minimumBet = minimumBet;
		this.maximumBet = maximumBet;
	}

	// Reset the wager to initial state
	@Override
	public void reset() {
		super.reset();
		this.amountBet = 0;
		this.amountWon = 0;
		this.insuranceBet = 0;
		this.insuranceWon = 0;
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
		amountBet = (Math.min(this.maximumBet, Math.max(this.minimumBet, bet)) + 1) / 2 * 2;
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

