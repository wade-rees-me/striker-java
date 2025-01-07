package me.rees.striker.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Shoe {
	//
	public static final String SPADES = "spades";
	public static final String DIAMONDS = "diamonds";
	public static final String CLUBS = "clubs";
	public static final String HEARTS = "hearts";

	//
	public static final String TWO = "two";
	public static final String THREE = "three";
	public static final String FOUR = "four";
	public static final String FIVE = "five";
	public static final String SIX = "six";
	public static final String SEVEN = "seven";
	public static final String EIGHT = "eight";
	public static final String NINE = "nine";
	public static final String TEN = "ten";
	public static final String JACK = "jack";
	public static final String QUEEN = "queen";
	public static final String KING = "king";
	public static final String ACE = "ace";

    public static final int MINIMUM_CARD_VALUE = 2;
    public static final int MAXIMUM_CARD_VALUE = 11;

	//
	private List<Card> cards;				// Cards currently in the shoe
	private String[] suits = {SPADES, DIAMONDS, CLUBS, HEARTS};
	private int numberOfCards;				// Total number of cards
	private int cutCard;					// The cut card position in the shoe
	private int burnCard = 1;
	private int nextCard;
	private int lastDiscard;
	private boolean forceShuffle = false;	// Flag to force a shuffle

	// Constructor: Create a new shoe of cards
	public Shoe(int numberOfDecks, float penetration) {
		cards = new ArrayList<>();
		for (int i = 0; i < numberOfDecks; i++) {
			for (int j = 0; j < suits.length; j++) {
				cards.add(new Card(suits[j], TWO, "2", 2));
				cards.add(new Card(suits[j], THREE, "3", 3));
				cards.add(new Card(suits[j], FOUR, "4", 4));
				cards.add(new Card(suits[j], FIVE, "5", 5));
				cards.add(new Card(suits[j], SIX, "6", 6));
				cards.add(new Card(suits[j], SEVEN, "7", 7));
				cards.add(new Card(suits[j], EIGHT, "8", 8));
				cards.add(new Card(suits[j], NINE, "9", 9));
				cards.add(new Card(suits[j], TEN, "X", 10));
				cards.add(new Card(suits[j], JACK, "X", 10));
				cards.add(new Card(suits[j], QUEEN, "X", 10));
				cards.add(new Card(suits[j], KING, "X", 10));
				cards.add(new Card(suits[j], ACE, "A", 11));
			}
		}

		numberOfCards = cards.size();
		nextCard = numberOfCards;
		lastDiscard = numberOfCards;
		cutCard = (int) (numberOfCards * penetration);

		shuffle();
	}

	//
	public void shuffle() {
		lastDiscard = numberOfCards;
		forceShuffle = false;
		shuffleRandom();
	}

	//
	private void shuffleRandom() {
		Collections.shuffle(cards, new Random());
		nextCard = burnCard;
	}

	//
	public Card drawCard() {
		if (nextCard >= numberOfCards) {
			forceShuffle = true;
			shuffleRandom();
		}
		Card card = cards.get(nextCard);
		nextCard++;
		return card;
	}

	//
	public boolean shouldShuffle() {
		lastDiscard = nextCard;
		return nextCard >= cutCard || forceShuffle;
	}

	//
	public int getNumberOfCards() {
		return numberOfCards;
	}

	//
	public boolean isAce(Card card) {
		return card.isAce();
	}

	// Display the cards in the shoe
	public void display() {
		int index = 0;
		System.out.println("--------------------------------------------------------------------------------");
		for (Card card : cards) {
			System.out.printf("%03d: ", index++);
			card.display();
		}
		System.out.println("--------------------------------------------------------------------------------");
	}
}

