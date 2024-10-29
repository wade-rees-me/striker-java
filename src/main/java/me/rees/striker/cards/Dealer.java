package me.rees.striker.cards;

public class Dealer {
	//
	private Hand hand;
	private boolean hitSoft17;

	// Constructor
	public Dealer(boolean hitSoft17) {
		this.hitSoft17 = hitSoft17;
		this.hand = new Hand();
	}

	//
	public void reset() {
		hand.reset();
	}

	// Draw a card and add it to the dealer's hand
	public void drawCard(Card card) {
		hand.drawCard(card);
	}

/*
	// Dealer's play logic
	public void play(Shoe shoe) {
		while (!shouldStand()) {
			drawCard(shoe.drawCard());
		}
	}
*/

	// Determine if the dealer should stand
	public boolean shouldStand() {
		if (hitSoft17 && hand.isSoft17()) {
			return false;
		}
		return hand.getHandTotal() >= 17;
	}

	//
	public Hand getHand() {
		return hand;
	}
}

