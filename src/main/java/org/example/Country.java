package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Country {

    public void initialize() {
        // Initialize the list to store country names fetched from the API
        List<String> countriesData = new ArrayList<>();

        // Fetch country data from the API and populate the countriesData list
        fetchDataFromAPI();
    }

    private void displayJsonData(String prefix, JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if (value.isValueNode()) {
                    System.out.println(prefix + key + " : " + value.asText());
                } else {
                    displayJsonData(prefix + key + " > ", value);
                }
            }
        } else if (jsonNode.isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                displayJsonData(prefix + "[" + i + "] > ", jsonNode.get(i));
            }
        }
    }



    private void fetchDataFromAPI() {
        // Fetch country data from the "all" endpoint
        String apiUrl = "https://restcountries.com/v3.1/all";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the API response and parse it as a JsonNode
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(conn.getInputStream());

                // Display all data present in the JSON response
                displayJsonData("", responseJson);
            } else {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        Country controller = new Country();
        controller.initialize();
    }
}
