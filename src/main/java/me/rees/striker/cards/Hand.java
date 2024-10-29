package me.rees.striker.cards;

import java.util.ArrayList;
import java.util.List;

public class Hand {
	//
	private List<Card> cards;
	private int handTotal = 0;				// Total value of the hand
	private int softAce = 0;				// Number of soft aces (11 valued aces)

	// Constructor
	public Hand() {
		reset();
	}

	// Reset the hand
	public void reset() {
		handTotal = 0;
		softAce = 0;
		cards = new ArrayList<>();
	}

	// Draw a card and add it to the hand
	public Card drawCard(Card card) {
		cards.add(card);
		calculateTotal();
		return card;
	}

	// Check if the hand is a blackjack
	public boolean isBlackjack() {
		return cards.size() == 2 && handTotal == 21;
	}

	// Check if the hand is a pair
	public boolean isPair() {
		return cards.size() == 2 && cards.get(0).getRank().equals(cards.get(1).getRank());
	}

	// Get the first card of a pair
	public Card getCardPair() {
		return cards.get(0);
	}

	// Check if the hand is a pair of aces
	public boolean isPairOfAces() {
		return isPair() && cards.get(0).getRank().equals("ace");
	}

	// Check if the hand is busted
	public boolean isBusted() {
		return handTotal > 21;
	}

	// Check if the hand has a soft ace
	public boolean isSoft() {
		return softAce > 0;
	}

	// Check if the hand is a soft 17
	public boolean isSoft17() {
		return handTotal == 17 && isSoft();
	}

	// Split a pair from the hand
	public Card splitPair() {
		if (isPair()) {
			Card card = cards.remove(cards.size() - 1);
			calculateTotal();
			return card;
		} else {
			throw new IllegalStateException("Error: Trying to split a non-pair");
		}
	}

	// Get the hand total
	public int getHandTotal() {
		return handTotal;
	}

	// Calculate the total value of the hand
	private void calculateTotal() {
		handTotal = 0;
		softAce = 0;

		for (Card card : cards) {
			handTotal += card.getValue();
			if (card.getValue() == 11) {
				softAce++;
			}
		}

		// Adjust the hand total if it's over 21 and there are soft aces
		while (handTotal > 21 && softAce > 0) {
			handTotal -= 10;
			softAce--;
		}
	}
}

