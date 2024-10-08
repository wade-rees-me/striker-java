package me.rees.striker.table;

import me.rees.striker.logger.Logger;
import me.rees.striker.constants.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//
public class Rules {
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
    public void rulesLoadTable(String decks) {
        try {
            rulesFetchTable("http://" + Constants.getRulesUrl() + "/" + decks);
        } catch (Exception e) {
            System.err.println("Error fetching rules table: " + e.getMessage());
            System.exit(1);
        }
    }

    //
    private void rulesFetchTable(String url) throws Exception {
        JsonObject json = httpGet(url);

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
    public JsonObject httpGet(String url) {
        HttpURLConnection connection = null;
        try {
            URL obj = new URL(url);
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON using Gson
            JsonObject json = (new JsonParser()).parse(response.toString()).getAsJsonObject();
            return json;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HTTP request failed");

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    //
    public void print(Logger logger) {
        logger.simulation(String.format("    %-24s\n", "Table Rules"));
        logger.simulation(String.format("      %-24s: %s\n", "Table", playbook));
        logger.simulation(String.format("      %-24s: %s\n", "Hit soft 17", hitSoft17 ? "true" : "false"));
        logger.simulation(String.format("      %-24s: %s\n", "Surrender", surrender ? "true" : "false"));
        logger.simulation(String.format("      %-24s: %s\n", "Double any two cards", doubleAnyTwoCards ? "true" : "false"));
        logger.simulation(String.format("      %-24s: %s\n", "Double after split", doubleAfterSplit ? "true" : "false"));
        logger.simulation(String.format("      %-24s: %s\n", "Resplit aces", resplitAces ? "true" : "false"));
        logger.simulation(String.format("      %-24s: %s\n", "Hit split aces", hitSplitAces ? "true" : "false"));
        logger.simulation(String.format("      %-24s: %d\n", "Blackjack bets", blackjackBets));
        logger.simulation(String.format("      %-24s: %d\n", "Blackjack pays", blackjackPays));
        logger.simulation(String.format("      %-24s: %.3f %%\n", "Penetration", penetration));
    }
}
