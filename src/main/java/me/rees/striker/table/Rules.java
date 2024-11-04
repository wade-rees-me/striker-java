package me.rees.striker.table;

import me.rees.striker.constants.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

        String payloadString = json.get("payload").getAsString();
		JsonObject payload = (new JsonParser()).parse(payloadString).getAsJsonObject();

        this.playbook = payload.has(PLAYBOOK) ? payload.get(PLAYBOOK).getAsString() : "missing";
        this.hitSoft17 = payload.has(HIT_SOFT_17) ? payload.get(HIT_SOFT_17).getAsBoolean() :  false;
        this.surrender = payload.has(SURRENDER) ? payload.get(SURRENDER).getAsBoolean() :  false;
        this.doubleAnyTwoCards = payload.has(DOUBLE_ANY_TWO_CARDS) ? payload.get(DOUBLE_ANY_TWO_CARDS).getAsBoolean() :  false;
        this.doubleAfterSplit = payload.has(DOUBLE_AFTER_SPLIT) ? payload.get(DOUBLE_AFTER_SPLIT).getAsBoolean() :  false;
        this.resplitAces = payload.has(RESPLIT_ACES) ? payload.get(RESPLIT_ACES).getAsBoolean() :  false;
        this.hitSplitAces = payload.has(HIT_SPLIT_ACES) ? payload.get(HIT_SPLIT_ACES).getAsBoolean() :  false;
        this.blackjackBets = payload.has(BLACKJACK_BETS) ? payload.get(BLACKJACK_BETS).getAsInt() :  5;
        this.blackjackPays = payload.has(BLACKJACK_PAYS) ? payload.get(BLACKJACK_PAYS).getAsInt() :  7;
        this.penetration = payload.has(PENETRATION) ? (float)payload.get(PENETRATION).getAsFloat() : (float).50;
    }

    //
    public void print() {
        System.out.println(String.format("    %-24s", "Table Rules"));
        System.out.println(String.format("      %-24s: %s", "Table", playbook));
        System.out.println(String.format("      %-24s: %s", "Hit soft 17", hitSoft17 ? TRUE : FALSE));
        System.out.println(String.format("      %-24s: %s", "Surrender", surrender ? TRUE : FALSE));
        System.out.println(String.format("      %-24s: %s", "Double any two cards", doubleAnyTwoCards ? TRUE : FALSE));
        System.out.println(String.format("      %-24s: %s", "Double after split", doubleAfterSplit ? TRUE : FALSE));
        System.out.println(String.format("      %-24s: %s", "Resplit aces", resplitAces ? TRUE : FALSE));
        System.out.println(String.format("      %-24s: %s", "Hit split aces", hitSplitAces ? TRUE : FALSE));
        System.out.println(String.format("      %-24s: %d", "Blackjack bets", blackjackBets));
        System.out.println(String.format("      %-24s: %d", "Blackjack pays", blackjackPays));
        System.out.println(String.format("      %-24s: %.3f %%", PENETRATION, penetration));
    }

    //
    public String serialize() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
		json.append(String.format("\"hit_soft_17\": \"%s\",\n", hitSoft17 ? TRUE : FALSE));
		json.append(String.format("\"surrender\": \"%s\",\n", surrender ? TRUE : FALSE));
		json.append(String.format("\"double_any_two_cards\": \"%s\",\n", doubleAnyTwoCards ? TRUE : FALSE));
		json.append(String.format("\"double_after_split\": \"%s\",\n", doubleAfterSplit ? TRUE : FALSE));
		json.append(String.format("\"resplit_aces\": \"%s\",\n", resplitAces ? TRUE : FALSE));
		json.append(String.format("\"hit_split_aces\": \"%s\",\n", hitSplitAces ? TRUE : FALSE));
		json.append(String.format("\"blackjack_bets\": \"%d\",\n", blackjackBets));
		json.append(String.format("\"blackjack_pays\": \"%d\",\n", blackjackPays));
		json.append(String.format("\"penetration\": \"%f\",\n", penetration));
        json.append("}");
        return json.toString();
    }
}

