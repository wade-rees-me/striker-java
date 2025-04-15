package me.rees.striker.simulator;

import me.rees.striker.arguments.Parameters;
import me.rees.striker.arguments.Report;
import me.rees.striker.cards.Card;
import me.rees.striker.cards.Dealer;
import me.rees.striker.cards.Hand;
import me.rees.striker.cards.Shoe;
import me.rees.striker.table.Rules;
import me.rees.striker.table.Strategy;
import me.rees.striker.constants.Constants;

public class Table {
	//
	private Parameters parameters;
	private Shoe shoe;
	private Dealer dealer;
	private Player player;
	private Report report;
	private Card up;
	private Card down;

	//
	public Table(Parameters parameters, Rules rules, Strategy strategy) {
		this.parameters = parameters;
		this.shoe = new Shoe(parameters.getNumberOfDecks(), rules.getPenetration());
		this.dealer = new Dealer(rules.isHitSoft17());
		this.player = new Player(rules, strategy, shoe.getNumberOfCards());
		this.report = new Report();
	}

	//
	public Player getPlayer() {
		return this.player;
	}

	//
	public Report getReport() {
		return this.report;
	}

	//
	public void session(boolean mimic) {
		while (report.getTotalHands() < parameters.getNumberOfHands()) {
			status(report.getTotalRounds(), report.getTotalHands());
			shoe.shuffle();
			player.shuffle();
			report.setTotalRounds(report.getTotalRounds() + 1);

			while (!shoe.shouldShuffle()) {
				report.setTotalHands(report.getTotalHands() + 1);
				dealer.reset();
				player.placeBet(mimic);

				dealCards(player.getWager());
				if (!mimic && up.isAce()) {
					player.insurance();
				}

				if (!dealer.getHand().isBlackjack()) {
					player.play(up, shoe, mimic);
					if (!player.bustedOrBlackjack()) {
						while (!dealer.shouldStand()) {
							Card card = shoe.drawCard();
							dealer.drawCard(card);
							player.showCard(card);
						}
					}
				}

				player.showCard(down);
				player.payoff(dealer.getHand().isBlackjack(),
				    dealer.getHand().isBusted(), dealer.getHand().getHandTotal());
			}
		}
		System.out.print("\r");
	}

	public void dealCards(Hand hand) {
		player.drawCard(hand, shoe.drawCard());
		up = shoe.drawCard();
		dealer.drawCard(up);
		player.showCard(up);

		player.drawCard(hand, shoe.drawCard());
		down = shoe.drawCard();
		dealer.drawCard(down);
	}

	public void show(Card card) {
		player.showCard(card);
	}

	private void status(long round, long hand) {
		if (round % Constants.STATUS_ROUNDS == 0) {
			System.out.print(String.format("\r    Rounds: [%,17d] Hands: [%,17d] Simulating...", round, hand));
			System.out.flush();
		}
	}
}
