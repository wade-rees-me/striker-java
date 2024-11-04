package me.rees.striker.table;

import me.rees.striker.cards.Card;
import me.rees.striker.constants.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

//
public class Strategy extends Request {
    public String Playbook;
    public List<Integer> Counts = new ArrayList<>();
    public List<Integer> Bets = new ArrayList<>();
    public String Insurance;
    public Map<String, List<String>> SoftDouble = new HashMap<>();
    public Map<String, List<String>> HardDouble = new HashMap<>();
    public Map<String, List<String>> PairSplit = new HashMap<>();
    public Map<String, List<String>> SoftStand = new HashMap<>();
    public Map<String, List<String>> HardStand = new HashMap<>();
    private int numberOfCards;

	//
    public Strategy(String decks, String strategy, int numberOfCards) {
        this.numberOfCards = numberOfCards;
        if (!"mimic".equalsIgnoreCase(strategy)) {
        	try {
            	JsonArray tables = strategyFetchTable("http://localhost:57910/striker/v1/strategy");
            	fetchTable(tables, decks, strategy);
        	} catch (Exception e) {
            	System.err.println("Error fetching rules table: " + e.getMessage());
            	System.exit(1);
        	}
        }
    }

	//
    private JsonArray strategyFetchTable(String url) throws Exception {
        return (new JsonParser()).parse(httpGet(url)).getAsJsonArray();
	}

	//
    private void fetchTable(JsonArray jsonResponseArray, String decks, String strategy) {
        for (JsonElement item : jsonResponseArray) {
            JsonObject jsonObject = (JsonObject)item;
            if (jsonObject.has("playbook") && jsonObject.has("hand") && jsonObject.has("payload")) {
            	if (decks.equals(jsonObject.get("playbook").getAsString()) && strategy.equals(jsonObject.get("hand").getAsString())) {
					JsonObject jsonPayload = (new JsonParser()).parse(jsonObject.get("payload").getAsString()).getAsJsonObject();

                	Playbook = jsonPayload.get("playbook").getAsString();
                	Counts = toIntList(jsonPayload.get("counts").getAsJsonArray());
                	Bets = toIntList(jsonPayload.get("bets").getAsJsonArray());
                	Insurance = jsonPayload.get("insurance").getAsString();

                	SoftDouble = toMapList(jsonPayload.get("soft-double").getAsJsonObject());
                	HardDouble = toMapList(jsonPayload.get("hard-double").getAsJsonObject());
                	PairSplit = toMapList(jsonPayload.get("pair-split").getAsJsonObject());
                	SoftStand = toMapList(jsonPayload.get("soft-stand").getAsJsonObject());
                	HardStand = toMapList(jsonPayload.get("hard-stand").getAsJsonObject());
                	return;
            	}
            }
        }
    }

	//
    public int getBet(int[] seenCards) {
        return getTrueCount(seenCards, getRunningCount(seenCards)) * TRUE_COUNT_BET;
    }

	//
    public boolean getInsurance(int[] seenCards) {
        int trueCount = getTrueCount(seenCards, getRunningCount(seenCards));
        return processValue(Insurance, trueCount, false);
    }

	//
    public boolean getDouble(int[] seenCards, int total, boolean soft, Card up) {
        int trueCount = getTrueCount(seenCards, getRunningCount(seenCards));
        String key = Integer.toString(total);
        return processValue(soft ? SoftDouble.get(key).get(up.getOffset()) : HardDouble.get(key).get(up.getOffset()), trueCount, false);
    }

	//
    public boolean getSplit(int[] seenCards, Card pair, Card up) {
        int trueCount = getTrueCount(seenCards, getRunningCount(seenCards));
        String key = Integer.toString(pair.getValue());
        return processValue(PairSplit.get(key).get(up.getOffset()), trueCount, false);
    }

	//
    public boolean getStand(int[] seenCards, int total, boolean soft, Card up) {
        int trueCount = getTrueCount(seenCards, getRunningCount(seenCards));
        String key = Integer.toString(total);
        return processValue(soft ? SoftStand.get(key).get(up.getOffset()) : HardStand.get(key).get(up.getOffset()), trueCount, true);
    }

	//
    private int getRunningCount(int[] seenCards) {
        int running = 0;
        for (int i = 0; i <= 12; i++) {
            running += Counts.get(i) * seenCards[i];
        }
        return running;
    }

	//
    private int getTrueCount(int[] seenCards, int runningCount) {
        int unseen = numberOfCards;
        for (int i = 2; i <= 11; i++) {
            unseen -= seenCards[i];
        }
        return unseen > 0 ? (int) ((float)runningCount / ((float)unseen / (float)TRUE_COUNT_MULTIPLIER)) : 0;
    }

	//
    private boolean processValue(String value, int trueCount, boolean missingValue) {
        if (value == null || value.isEmpty()) {
            return missingValue;
        }
        switch (value.toLowerCase()) {
            case "yes":
            case "y":
                return true;
            case "no":
            case "n":
                return false;
            default:
                if (value.startsWith("R")) {
                    return trueCount <= Integer.parseInt(value.substring(1));
                }
                return trueCount >= Integer.parseInt(value);
        }
    }

	//
    private List<Integer> toIntList(JsonArray jsonArray) {
        List<Integer> list = new ArrayList<>();
		for (JsonElement element : jsonArray) {
            list.add(element.getAsInt());
		}
        return list;
    }

	//
	private Map<String, List<String>> toMapList(JsonObject jsonObject) {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), toStrList(entry.getValue().getAsJsonArray()));
        }
        return map;
    }

	//
    private List<String> toStrList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }
}

