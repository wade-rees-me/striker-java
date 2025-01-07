package me.rees.striker.table;

import me.rees.striker.cards.Card;
import me.rees.striker.cards.Shoe;
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
    public String Insurance;
    public List<Integer> Counts = new ArrayList<>();
    public Chart SoftDouble = new Chart("Soft Double");
    public Chart HardDouble = new Chart("Hard Double");
    public Chart PairSplit = new Chart("Pair Split");
    public Chart SoftStand = new Chart("Soft Stand");
    public Chart HardStand = new Chart("Hard Stand");
    private int numberOfCards;

	//
    public Strategy(String decks, String strategy, int numberOfCards) {
        this.numberOfCards = numberOfCards;
        if (!"mimic".equalsIgnoreCase(strategy)) {
        	try {
            	JsonArray tables = strategyFetchTable("http://localhost:57910/striker/v1/strategy");
            	fetchTable(tables, decks, strategy);

    			SoftDouble.print();
    			HardDouble.print();
    			PairSplit.print();
    			SoftStand.print();
    			HardStand.print();
    			printCounts();
        	} catch (Exception e) {
            	System.err.println("Error fetching strategy table: " + e.getMessage());
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
                	Insurance = jsonPayload.get("insurance").getAsString();
                	Counts = toIntList(jsonPayload.get("counts").getAsJsonArray());
					//Counts.add(0, 0);
					//Counts.add(0, 0);

                	toMapList(jsonPayload.get("soft-double").getAsJsonObject(), SoftDouble);
                	toMapList(jsonPayload.get("hard-double").getAsJsonObject(), HardDouble);
                	toMapList(jsonPayload.get("pair-split").getAsJsonObject(), PairSplit);
                	toMapList(jsonPayload.get("soft-stand").getAsJsonObject(), SoftStand);
                	toMapList(jsonPayload.get("hard-stand").getAsJsonObject(), HardStand);
                	return;
            	}
            }
        }
    }

	//
    public int getBet(int[] seenCards) {
        return getTrueCount(seenCards, getRunningCount(seenCards)) * Constants.TRUE_COUNT_BET;
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
        return processValue(soft ? SoftDouble.getValue(key, up.getValue()) : HardDouble.getValue(key, up.getValue()), trueCount, false);
    }

	//
    public boolean getSplit(int[] seenCards, Card pair, Card up) {
        int trueCount = getTrueCount(seenCards, getRunningCount(seenCards));
        String key = pair.getKey();
        return processValue(PairSplit.getValue(key, up.getValue()), trueCount, false);
    }

	//
    public boolean getStand(int[] seenCards, int total, boolean soft, Card up) {
        int trueCount = getTrueCount(seenCards, getRunningCount(seenCards));
        String key = Integer.toString(total);
        return processValue(soft ? SoftStand.getValue(key, up.getValue()) : HardStand.getValue(key, up.getValue()), trueCount, true);
    }

	//
    private int getRunningCount(int[] seenCards) {
        int running = 0;
        for (int i = 2; i <= 11; i++) {
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
        return unseen > 0 ? (int) ((float)runningCount / ((float)unseen / (float)Constants.TRUE_COUNT_MULTIPLIER)) : 0;
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
		list.add(0);
		list.add(0);
		for (JsonElement element : jsonArray) {
            list.add(element.getAsInt());
		}
        return list;
    }

	//
	private void toMapList(JsonObject jsonObject, Chart chart) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            toStrList(entry.getValue().getAsJsonArray(), entry.getKey(), chart);
        }
    }

	//
    private void toStrList(JsonArray jsonArray, String key, Chart chart) {
		int index = 0;
        for (JsonElement element : jsonArray) {
			chart.insert(key, index, element.getAsString());
			index++;
        }
    }

    //
    public void printCounts() {
        System.out.println("Counts");
        System.out.println("--------------------2-----3-----4-----5-----6-----7-----8-----9-----X-----A---");
		System.out.printf("     ");
		for (int value : Counts) {
			System.out.printf("%4d, ", value);
		}
		System.out.println();
        System.out.println("------------------------------------------------------------------------------");
    }
}

