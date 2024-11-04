package me.rees.striker.cards;

public class Card {
	//
	private String suit; // Suit of the card (e.g., "hearts")
	private String rank; // Rank of the card (e.g., "ace")
	private int value;   // Value of the card for game calculations
	private int offset;  // Index of the card in a suit

	// Constructor
	public Card(String suit, String rank, int value, int offset) {
		this.suit = suit;
		this.rank = rank;
		this.value = value;
		this.offset = offset;
	}

	//
	public boolean isAce() {
		return value == 11;
	}

	// Getters
	public String getRank() {
		return rank;
	}

	//
	public String getSuit() {
		return suit;
	}

	//
	public int getValue() {
		return value;
	}

	//
	public int getOffset() {
		return offset;
	}

	//
	public void display() {
		System.out.println(rank + " of " + suit + " {" + value + ", " + offset + "}");
	}
}

