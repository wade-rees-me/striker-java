package me.rees.striker.simulator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.rees.striker.table.Rules;
import me.rees.striker.arguments.Parameters;
import me.rees.striker.arguments.Arguments;
import me.rees.striker.simulator.Simulator;
import me.rees.striker.constants.Constants;
import me.rees.striker.cards.Card;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Strategy {

    private String playbook;
    private int numberOfCards;
    private JsonObject jsonObject;
    private String urlBet = "http://" + Constants.getStrategyUrl() + "/bet";
    private String urlInsurance = "http://" + Constants.getStrategyUrl() + "/insurance";
    private String urlSurrender = "http://" + Constants.getStrategyUrl() + "/surrender";
    private String urlDouble = "http://" + Constants.getStrategyUrl() + "/double";
    private String urlSplit = "http://" + Constants.getStrategyUrl() + "/split";
    private String urlStand = "http://" + Constants.getStrategyUrl() + "/stand";
    private String urlPlay = "http://" + Constants.getStrategyUrl() + "/play";

    public Strategy(String playbook, int numberOfCards) {
        this.playbook = playbook;
        this.numberOfCards = numberOfCards;
    }

    public int getBet(int[] seenCards) {
        jsonObject = null;
        return parseAuxInt(httpGet(urlBet, buildParams(seenCards, null, null, null)), "bet", 2);
    }

    public boolean getInsurance(int[] seenCards) {
        return parseAuxBool(httpGet(urlInsurance, buildParams(seenCards, null, null, null)), "insurance", false);
    }

    public boolean getSurrender(int[] seenCards, int[] haveCards, Card up) {
        if (jsonObject != null) {
            return parseAuxBool(jsonObject, "surrender", false);
        }
        return parseAuxBool(httpGet(urlSurrender, buildParams(seenCards, haveCards, null, up)), "surrender", false);
    }

    public boolean getDouble(int[] seenCards, int[] haveCards, Card up) {
        if (jsonObject != null) {
            return parseAuxBool(jsonObject, "double", false);
        }
        return parseAuxBool(httpGet(urlDouble, buildParams(seenCards, haveCards, null, up)), "double", false);
    }

    public boolean getSplit(int[] seenCards, Card pair, Card up) {
        if (jsonObject != null) {
            return parseAuxBool(jsonObject, "split", false);
        }
        return parseAuxBool(httpGet(urlSplit, buildParams(seenCards, null, pair, up)), "split", false);
    }

    public boolean getStand(int[] seenCards, int[] haveCards, Card up) {
        if (jsonObject != null) {
            return parseAuxBool(jsonObject, "stand", true);
        }
        return parseAuxBool(httpGet(urlStand, buildParams(seenCards, haveCards, null, up)), "stand", true);
    }

    public void doPlay(int[] seenCards, int[] haveCards, Card pair, Card up) {
        jsonObject = httpGet(urlPlay, buildParams(seenCards, haveCards, pair, up));
    }

    public void clear() {
        jsonObject = null;
    }

    private String buildParams(int[] seenData, int[] haveData, Card pair, Card up) {
        StringBuilder params = new StringBuilder("playbook=" + playbook);
        params.append("&cards=").append(numberOfCards);
        if (up != null) {
            params.append("&up=").append(up.getOffset());
        }
        if (pair != null) {
            params.append("&pair=").append(pair.getValue());
        }

        if (seenData != null) {
            JsonArray seenJson = new JsonArray();
            for (int card : seenData) {
                seenJson.add(card);
            }
            params.append("&seen=").append(seenJson.toString());
        }

        if (haveData != null) {
            JsonArray haveJson = new JsonArray();
            for (int card : haveData) {
                haveJson.add(card);
            }
            params.append("&have=").append(haveJson.toString());
        }

        return params.toString();
    }

    private JsonObject httpGet(String url, String params) {
        try {
            URL obj = new URL(url + "?" + params);
//System.out.println(url + "?" + params);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return (new JsonParser()).parse(response.toString()).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HTTP request failed");
        }
    }

    private int parseAuxInt(JsonObject json, String tag, int defaultValue) {
        return json.has(tag) ? json.get(tag).getAsInt() : defaultValue;
    }

    private boolean parseAuxBool(JsonObject json, String tag, boolean defaultValue) {
        return json.has(tag) ? json.get(tag).getAsBoolean() : defaultValue;
    }
}

