package com.example.ytScrapper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ytScrapper.services.ytService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ytController {


      @Autowired
      private ytService youTubeService;


       @GetMapping("/")
        public String getHome() {
            return "index";
        }

        @PostMapping("/vdetails")
        public String getYouTubeVideoDetails(@RequestParam String videoLink, Model model) {
            String videoId = youTubeService.extractVideoId(videoLink);
    
            if (videoId != null) {
                try {
                    JsonNode videoItem = youTubeService.getVideoDetails(videoId);
                    String title = videoItem.path("title").asText();
                    String description = videoItem.path("description").asText();
                    String thumbnailUrl = videoItem.path("thumbnails").path("standard").path("url").asText();
                    String[] tags = new ObjectMapper().treeToValue(videoItem.path("tags"), String[].class);

                    model.addAttribute("videoDetails", "test");
                    model.addAttribute("videoId", videoId);
                    model.addAttribute("title", title);
                    model.addAttribute("thumbnailUrl", thumbnailUrl);
                    model.addAttribute("tags", tags);
                    model.addAttribute("description", description);
    
                    return "details";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "redirect:/youtube?invalidLink=true";
                }
            } else {
                return "redirect:/youtube?invalidLink=true";
            }
    }
    
 

}
