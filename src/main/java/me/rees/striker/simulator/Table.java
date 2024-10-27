package me.rees.striker.simulator;

import me.rees.striker.table.Rules;
import me.rees.striker.table.Strategy;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.arguments.Arguments;
import me.rees.striker.arguments.Report;
import me.rees.striker.simulator.Simulator;
import me.rees.striker.constants.Constants;
import me.rees.striker.cards.Hand;
import me.rees.striker.cards.Shoe;
import me.rees.striker.cards.Card;
import me.rees.striker.cards.Dealer;

import java.util.TimeZone;
import java.util.Date;
import java.time.Instant;
import java.text.SimpleDateFormat;

public class Table {
	//
	private Parameters parameters;
	private Shoe shoe;
	private Dealer dealer;
	private Player player;
	private Report report;

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
		System.out.println(String.format("      Start: table, playing %s hands", parameters.getNumberOfHands()));

		report.setStart(Instant.now());

		while (report.getTotalHands() < parameters.getNumberOfHands()) {
			status(report.getTotalRounds(), report.getTotalHands());
			shoe.shuffle();
			player.shuffle();
			report.setTotalRounds(report.getTotalRounds() + 1);

			while (!shoe.shouldShuffle()) {
				report.setTotalHands(report.getTotalHands() + 1);
				dealer.reset();
				player.placeBet(mimic);

				Card up = dealCards(player.getWager());
				if (!mimic && up.isAce()) {
					player.insurance();
				}

				if (!dealer.getHand().isBlackjack()) {
					player.play(up, shoe, mimic);
					if (!player.bustedOrBlackjack()) {
						dealer.play(shoe);
					}
				}

				player.payoff(dealer.getHand().isBlackjack(), dealer.getHand().isBusted(), dealer.getHand().getHandTotal());
				player.showCard(up);
			}
		}
		System.out.println();

		report.setEnd(Instant.now());
		long duration = report.getEnd().getEpochSecond() - report.getStart().getEpochSecond();
		report.setDuration(duration);

		System.out.println("      End: table");
	}

	public Card dealCards(Hand hand) {
		player.drawCard(hand, shoe.drawCard());
		Card up = shoe.drawCard();
		dealer.drawCard(up);
		player.drawCard(hand, shoe.drawCard());
		dealer.drawCard(shoe.drawCard());
		player.showCard(up);
		return up;
	}

	public void show(Card card) {
		player.showCard(card);
	}

	private void status(long round, long hand) {
		if (round == 0) {
			System.out.print("        ");
		}
		if ((round + 1) % Constants.STATUS_DOT == 0) {
			System.out.print(".");
		}
		if ((round + 1) % Constants.STATUS_LINE == 0) {
			System.out.println(String.format(" : %d (rounds), %d (hands)", (round + 1), hand));
			System.out.print("        ");
		}
	}
}

