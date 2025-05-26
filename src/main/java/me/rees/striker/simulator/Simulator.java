package me.rees.striker.simulator;

import java.io.IOException;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.arguments.Report;
import me.rees.striker.table.Rules;
import me.rees.striker.table.Strategy;

public class Simulator {
  //
  private Parameters parameters;
  private Rules rules;
  private Table table;
  private Report report;

  //
  public Simulator(Parameters parameters, Rules rules, Strategy strategy) {
    this.parameters = parameters;
    this.rules = rules;
    this.table = new Table(parameters, rules, strategy);
    this.report = new Report();
  }

  public Report getReport() {
    return this.report;
  }

  // The main simulator process
  public void simulatorRunOnce() throws IOException {
    simulatorRunSimulation();
  }

  // Run the simulation
  private void simulatorRunSimulation() throws IOException {
    table.session(parameters.getStrategy().equals("mimic"));

    report.setTotalBet(table.getPlayer().getReport().getTotalBet());
    report.setTotalWon(table.getPlayer().getReport().getTotalWon());
    report.setTotalBlackjacks(table.getPlayer().getReport().getTotalBlackjacks());
    report.setTotalDoubles(table.getPlayer().getReport().getTotalDoubles());
    report.setTotalSplits(table.getPlayer().getReport().getTotalSplits());
    report.setTotalSplitsAce(table.getPlayer().getReport().getTotalSplitsAce());
    report.setTotalWins(table.getPlayer().getReport().getTotalWins());
    report.setTotalPushes(table.getPlayer().getReport().getTotalPushes());
    report.setTotalLoses(table.getPlayer().getReport().getTotalLoses());
    report.setTotalRounds(table.getReport().getTotalRounds());
    report.setTotalHands(table.getReport().getTotalHands());
  }
}
