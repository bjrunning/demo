package com.example.controller;

import com.example.dto.topic.TopicDTO;
import com.example.service.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/")
    public String home(Model model,  @RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String query) {
        if (query != null && !query.isEmpty()) {
            Pageable pageable = PageRequest.of(page, 6);
            Page<TopicDTO> topics = topicService.searchByTitle(query, pageable);
            model.addAttribute("topics", topics);
            model.addAttribute("currentPage", page);
            return "index";
        }
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "other/about";
    }

    @GetMapping("/privacy_policy")
    public String privacyPolicy() {
        return "other/privacy_policy";
    }

    @GetMapping("/rules")
    public String rules() {
        return "other/rules";
    }
}
