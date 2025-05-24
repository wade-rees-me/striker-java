package me.rees.striker;

import me.rees.striker.arguments.Arguments;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.arguments.Report;
import me.rees.striker.simulator.Simulator;
import me.rees.striker.table.Rules;
import me.rees.striker.table.Strategy;
import me.rees.striker.xlog.Xlog;

//
public class Main {
  //
  public static void main(String[] args) {
    try {
        if (!Xlog.initSyslog("192.168.0.27", 10514)) {
            System.err.println("Unable to initialize syslog");
            return;
        }
      Arguments arguments = new Arguments(args);
      Parameters parameters =
          new Parameters(
              arguments.getDecks(),
              arguments.getStrategy(),
              arguments.getNumberOfDecks(),
              arguments.getHands());
      Rules rules = new Rules(arguments.getDecks());
      Strategy strategy =
          new Strategy(
              arguments.getDecks(), arguments.getStrategy(), arguments.getNumberOfDecks() * 52);
      Simulator simulator = new Simulator(parameters, rules, strategy);
      Report finalReport = new Report();


        Xlog.logInfo("Simulation started at %s", "now");
        Xlog.logError("An error occurred: %s", "card shuffle failed");
        Xlog.logFatal("Fatal: Simulation crashed at %s", "now");


      System.out.println(String.format("  Start: %s", parameters.getSimulator()));
      System.out.println(
          "  -- arguments -------------------------------------------------------------------");
      parameters.print();
      rules.print();
      System.out.println(
          "  --------------------------------------------------------------------------------");

      finalReport.init(parameters);
      simulator.simulatorRunOnce();
      finalReport.merge(simulator.getReport());
      finalReport.finish();

      System.out.println(
          "  -- results ---------------------------------------------------------------------");
      finalReport.print();
      System.out.println(
          "  --------------------------------------------------------------------------------");
      System.out.println(
          "  -- insert ----------------------------------------------------------------------");
      finalReport.insert();
      System.out.println(
          "  --------------------------------------------------------------------------------");

    } catch (Exception ex) {
      System.out.println(String.format("  Exception: %s", ex.getMessage()));
    }
        Xlog.closeSyslog();
  }
}

