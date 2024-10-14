package me.rees.striker.simulator;

import me.rees.striker.table.Rules;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.arguments.Arguments;
import me.rees.striker.arguments.Report;
import me.rees.striker.simulator.Simulator;
import me.rees.striker.constants.Constants;
import me.rees.striker.cards.Wager;
import me.rees.striker.cards.Hand;
import me.rees.striker.cards.Shoe;
import me.rees.striker.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private Parameters parameters;
	private Rules rules;
	private int numberOfCards;
	private Wager wager;
	private List<Wager> splits;
	private Strategy strategy;
	private Report report;
	private int[] seenCards;

	// Constructor for Player
	public Player(Parameters parameters, Rules rules, int numberOfCards) {
		this.parameters = parameters;
		this.rules = rules;
		this.numberOfCards = numberOfCards;
		this.wager = new Wager();
		this.splits = new ArrayList<>();
		this.strategy = new Strategy(parameters.getPlaybook(), numberOfCards);
		this.report = new Report();
		this.seenCards = new int[13]; // Keeps track of the cards the player has seen
	}

	public Wager getWager() {
		return this.wager;
	}

	public Report getReport() {
		return this.report;
	}

	// Shuffle function (reinitializes seen cards)
	public void shuffle() {
		for (int i = 0; i < seenCards.length; i++) {
			seenCards[i] = 0;
		}
	}

	// Place a bet for the player
	public void placeBet(boolean mimic) {
		splits.clear();
		wager.reset();
		wager.setAmountBet(mimic ? Constants.MINIMUM_BET : strategy.getBet(seenCards));
	}

	// Simulate an insurance bet
	public void insurance() {
		if (strategy.getInsurance(seenCards)) {
			wager.setInsuranceBet(wager.getAmountBet() / 2);
		}
	}

	// Play the hand
	public void play(Card up, Shoe shoe, boolean mimic) {
		if (wager.isBlackjack()) {
			return;
		}

		if (mimic) {
			while (!mimicStand()) {
				drawCard(wager, shoe.drawCard());
			}
			return;
		}

		strategy.doPlay(seenCards, wager.getHaveCards(), wager.isPair() ? wager.getCardPair() : null, up);
		if (rules.isSurrender() && strategy.getSurrender(seenCards, wager.getHaveCards(), up)) {
			strategy.clear();
			wager.surrender();
			return;
		}

		if ((rules.isDoubleAnyTwoCards() || wager.getHandTotal() == 10 || wager.getHandTotal() == 11) && strategy.getDouble(seenCards, wager.getHaveCards(), up)) {
			strategy.clear();
			wager.doubleBet();
			drawCard(wager, shoe.drawCard());
			return;
		}

		if (wager.isPair() && strategy.getSplit(seenCards, wager.getCardPair(), up)) {
			strategy.clear();
			Wager split = new Wager();
			wager.splitHand(split);
			splits.add(split);

			if (wager.isPairOfAces()) {
				if (!rules.isResplitAces() && !rules.isHitSplitAces()) {
					drawCard(wager, shoe.drawCard());
					drawCard(split, shoe.drawCard());
					return;
				}
			}

			drawCard(wager, shoe.drawCard());
			playSplit(wager, shoe, up);
			drawCard(split, shoe.drawCard());
			playSplit(split, shoe, up);
			return;
		}

		boolean doStand = strategy.getStand(seenCards, wager.getHaveCards(), up);
		strategy.clear();
		while (!wager.isBusted() && !doStand) {
			drawCard(wager, shoe.drawCard());
			doStand = strategy.getStand(seenCards, wager.getHaveCards(), up);
		}
	}

	// Play the split hand
	private void playSplit(Wager wager, Shoe shoe, Card up) {
		if (rules.isDoubleAfterSplit() && strategy.getDouble(seenCards, wager.getHaveCards(), up)) {
			wager.doubleBet();
			drawCard(wager, shoe.drawCard());
			return;
		}

		if (wager.isPair()) {
			if (wager.isPairOfAces()) {
				if (rules.isResplitAces() && strategy.getSplit(seenCards, wager.getCardPair(), up)) {
					Wager split = new Wager();
					splits.add(split);
					wager.splitHand(split);
					drawCard(wager, shoe.drawCard());
					playSplit(wager, shoe, up);
					drawCard(split, shoe.drawCard());
					playSplit(split, shoe, up);
					return;
				}
			} else if (strategy.getSplit(seenCards, wager.getCardPair(), up)) {
				Wager split = new Wager();
				splits.add(split);
				wager.splitHand(split);
				drawCard(wager, shoe.drawCard());
				playSplit(wager, shoe, up);
				drawCard(split, shoe.drawCard());
				playSplit(split, shoe, up);
				return;
			}
		}

		boolean doStand = strategy.getStand(seenCards, wager.getHaveCards(), up);
		while (!wager.isBusted() && !doStand) {
			drawCard(wager, shoe.drawCard());
			doStand = strategy.getStand(seenCards, wager.getHaveCards(), up);
		}
	}

	// Draw a card
	public void drawCard(Hand hand, Card card) {
		showCard(card);
		hand.drawCard(card);
	}

	// Show the card
	public void showCard(Card card) {
		seenCards[card.getOffset()]++;
	}

	// Check if player busted or has blackjack
	public boolean bustedOrBlackjack() {
		if (splits.isEmpty()) {
			return wager.isBusted() || wager.isBlackjack();
		}

		for (Wager split : splits) {
			if (!split.isBusted()) {
				return false;
			}
		}
		return true;
	}

	// Payoff for the player
	public void payoff(boolean dealerBlackjack, boolean dealerBusted, int dealerTotal) {
		if (splits.isEmpty()) {
			payoffHand(wager, dealerBlackjack, dealerBusted, dealerTotal);
		} else {
			for (Wager split : splits) {
				payoffSplit(split, dealerBusted, dealerTotal);
			}
		}
	}

	// Payoff for the hand
	private void payoffHand(Wager wager, boolean dealerBlackjack, boolean dealerBusted, int dealerTotal) {
		if (wager.didSurrender()) {
			report.setTotalBet(report.getTotalBet() + wager.getAmountBet());
			report.setTotalWon(report.getTotalWon() - wager.getAmountBet() / 2);
			return;
		}

		if (dealerBlackjack) {
			wager.wonInsurance();
			if (wager.isBlackjack()) {
				wager.push();
			} else {
				wager.lost();
			}
		} else {
			wager.lostInsurance();
			if (wager.isBlackjack()) {
				wager.wonBlackjack(rules.getBlackjackPays(), rules.getBlackjackBets());
			} else if (wager.isBusted()) {
				wager.lost();
			} else if (dealerBusted || wager.getHandTotal() > dealerTotal) {
				wager.won();
			} else if (dealerTotal > wager.getHandTotal()) {
				wager.lost();
			} else {
				wager.push();
			}
		}

		report.setTotalBet(report.getTotalBet() + wager.getAmountBet() + wager.getInsuranceBet());
		report.setTotalWon(report.getTotalWon() + wager.getAmountWon() + wager.getInsuranceWon());
	}

	// Payoff for split hands
	private void payoffSplit(Wager wager, boolean dealerBusted, int dealerTotal) {
		if (wager.isBusted()) {
			wager.lost();
		} else if (dealerBusted || wager.getHandTotal() > dealerTotal) {
			wager.won();
		} else if (dealerTotal > wager.getHandTotal()) {
			wager.lost();
		} else {
			wager.push();
		}

		report.setTotalWon(report.getTotalWon() + wager.getAmountWon());
		report.setTotalBet(report.getTotalBet() + wager.getAmountBet());
	}

	// Mimic strategy stand
	private boolean mimicStand() {
		return !wager.isSoft17() && wager.getHandTotal() >= 17;
	}
}

