package me.rees.striker.table;

import me.rees.striker.constants.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//
public class Request {
	//
    public String httpGet(String url) {
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

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HTTP request failed");

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

