package com.example.ytScrapper.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ytScrapper.config.ytConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.*;

@Service
public class ytService {

    @Autowired
    private ytConfig appConfig;

    public JsonNode getVideoDetails(String videoId) throws Exception {
        String apiUrl = appConfig.getApiUrl();
        String apiKeyParam = "key=" + appConfig.getApiKey();
        String partParam = "part=snippet";
        String idParam = "id=" + videoId;

        String url = apiUrl + "?" + apiKeyParam + "&" + partParam + "&" + idParam;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response).path("items").get(0).path("snippet");
    }

    public String extractVideoId(String youtubeLink) {
        // Pattern for standard YouTube URL
        Pattern pattern1 = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed\\.|youtu\\.be\\/|\\/v\\/|\\/e\\/|watch\\?v=|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed\\.)([^\"&?\\/\\s]{11})", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(youtubeLink);

        // Pattern for short YouTube URL
        Pattern pattern2 = Pattern.compile("youtu.be\\/(.{11})", Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(youtubeLink);

        if (matcher1.find()) {
            return matcher1.group(1);
        } else if (matcher2.find()) {
            return matcher2.group(1);
        }

        return null;
    }
    
}
