package me.rees.striker.cards;

public class Card {
	//
	private String suit; // Suit of the card (e.g., "hearts")
	private String rank; // Rank of the card (e.g., "ace")
	private String key;
	private int value;   // Value of the card for game calculations

	// Constructor
	public Card(String suit, String rank, String key, int value) {
		this.suit = suit;
		this.rank = rank;
		this.key = key;
		this.value = value;
	}

	//
	public boolean isAce() {
		return value == 11;
	}

	//
	public String getSuit() {
		return suit;
	}

	// Getters
	public String getRank() {
		return rank;
	}

	// Getters
	public String getKey() {
		return key;
	}

	//
	public int getValue() {
		return value;
	}

	//
	public void display() {
		System.out.println(rank + " of " + suit + " {" + value + "}");
	}
}

