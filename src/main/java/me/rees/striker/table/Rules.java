package me.rees.striker.table;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.rees.striker.constants.Constants;

//
public class Rules extends Request {
  //
  private static final String PLAYBOOK = "playbook";
  private static final String HIT_SOFT_17 = "hitSoft17";
  private static final String SURRENDER = "surrender";
  private static final String DOUBLE_ANY_TWO_CARDS = "doubleAnyTwoCards";
  private static final String DOUBLE_AFTER_SPLIT = "doubleAfterSplit";
  private static final String RESPLIT_ACES = "resplitAces";
  private static final String HIT_SPLIT_ACES = "hitSplitAces";
  private static final String BLACKJACK_PAYS = "blackjackPays";
  private static final String BLACKJACK_BETS = "blackjackBets";
  private static final String PENETRATION = "penetration";
  private static final String TRUE = "true";
  private static final String FALSE = "false";

  //
  private String playbook;
  private boolean hitSoft17 = true;
  private boolean surrender = false;
  private boolean doubleAnyTwoCards = true;
  private boolean doubleAfterSplit = false;
  private boolean resplitAces = false;
  private boolean hitSplitAces = false;
  private int blackjackPays = 5;
  private int blackjackBets = 3;
  private float penetration = 0.70f;

  //
  public Rules(String decks) {
    try {
      rulesFetchTable("http://" + Constants.getRulesUrl() + "/" + decks);
    } catch (Exception e) {
      System.err.println("Error fetching rules table: " + e.getMessage());
      System.exit(1);
    }
  }

  //
  public String getPlaybook() {
    return this.playbook;
  }

  public boolean isHitSoft17() {
    return this.hitSoft17;
  }

  public boolean isSurrender() {
    return this.surrender;
  }

  public boolean isDoubleAnyTwoCards() {
    return this.doubleAnyTwoCards;
  }

  public boolean isDoubleAfterSplit() {
    return this.doubleAfterSplit;
  }

  public boolean isResplitAces() {
    return this.resplitAces;
  }

  public boolean isHitSplitAces() {
    return this.hitSplitAces;
  }

  public int getBlackjackPays() {
    return this.blackjackPays;
  }

  public int getBlackjackBets() {
    return this.blackjackBets;
  }

  public float getPenetration() {
    return this.penetration;
  }

  //
  private void rulesFetchTable(String url) throws Exception {
    JsonObject json = (new JsonParser()).parse(httpGet(url)).getAsJsonObject();

    this.playbook = json.has(PLAYBOOK) ? json.get(PLAYBOOK).getAsString() : "missing";
    this.hitSoft17 = json.has(HIT_SOFT_17) ? json.get(HIT_SOFT_17).getAsBoolean() : false;
    this.surrender = json.has(SURRENDER) ? json.get(SURRENDER).getAsBoolean() : false;
    this.doubleAnyTwoCards =
        json.has(DOUBLE_ANY_TWO_CARDS) ? json.get(DOUBLE_ANY_TWO_CARDS).getAsBoolean() : false;
    this.doubleAfterSplit =
        json.has(DOUBLE_AFTER_SPLIT) ? json.get(DOUBLE_AFTER_SPLIT).getAsBoolean() : false;
    this.resplitAces = json.has(RESPLIT_ACES) ? json.get(RESPLIT_ACES).getAsBoolean() : false;
    this.hitSplitAces = json.has(HIT_SPLIT_ACES) ? json.get(HIT_SPLIT_ACES).getAsBoolean() : false;
    this.blackjackBets = json.has(BLACKJACK_BETS) ? json.get(BLACKJACK_BETS).getAsInt() : 5;
    this.blackjackPays = json.has(BLACKJACK_PAYS) ? json.get(BLACKJACK_PAYS).getAsInt() : 7;
    this.penetration =
        json.has(PENETRATION) ? (float) json.get(PENETRATION).getAsFloat() : (float) .50;
  }

  //
  public void print() {
    System.out.println(String.format("    %-24s", "Table Rules"));
    System.out.println(String.format("      %-24s: %s", "Table", playbook));
    System.out.println(String.format("      %-24s: %s", "Hit soft 17", hitSoft17 ? TRUE : FALSE));
    System.out.println(String.format("      %-24s: %s", "Surrender", surrender ? TRUE : FALSE));
    System.out.println(
        String.format("      %-24s: %s", "Double any two cards", doubleAnyTwoCards ? TRUE : FALSE));
    System.out.println(
        String.format("      %-24s: %s", "Double after split", doubleAfterSplit ? TRUE : FALSE));
    System.out.println(
        String.format("      %-24s: %s", "Resplit aces", resplitAces ? TRUE : FALSE));
    System.out.println(
        String.format("      %-24s: %s", "Hit split aces", hitSplitAces ? TRUE : FALSE));
    System.out.println(String.format("      %-24s: %d", "Blackjack bets", blackjackBets));
    System.out.println(String.format("      %-24s: %d", "Blackjack pays", blackjackPays));
    System.out.println(String.format("      %-24s: %.3f %%", PENETRATION, penetration));
  }
}
