package org.example.keycloak;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.keycloak.external.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserApiService {
    public Logger logger = LoggerFactory.getLogger(UserApiService.class);
    ObjectMapper objectMapper = new ObjectMapper();

    public List<User> getAllUsers(String url) throws NullPointerException {
        List<User> userList = new ArrayList<>();

        try {
            URL userApiUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) userApiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JsonNode jsonResponse  = objectMapper.readTree(response.toString());

                if(jsonResponse.isArray()){
                    for(JsonNode jsonNode: jsonResponse){
                        Long id = jsonNode.has("id") ? jsonNode.get("id").asLong() : null;
                        String username = jsonNode.has("username") ?jsonNode.get("username").asText():null;
                        String firstName = jsonNode.has("first_name") ? jsonNode.get("first_name").asText() :null ;
                        String lastName = jsonNode.has("last_name") ? jsonNode.get("last_name").asText() : null ;

                        User user = new User(id,username,firstName,lastName);
                        userList.add(user);
                    }
                }
            } else {
                logger.error("HTTP request failed with response code: " +responseCode);
            }
            connection.disconnect();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
}
