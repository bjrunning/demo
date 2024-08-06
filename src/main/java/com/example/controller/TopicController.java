package com.example.controller;

import com.example.model.Topic;
import com.example.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public String listTopics(Model model) {
        model.addAttribute("topics", topicService.findAll());
        return "topics/topics";
    }

    @GetMapping("/create")
    public String createTopic(Model model) {
        model.addAttribute("topic", new Topic());
        return "topics/createTopic";
    }

    @PostMapping("/create")
    public String saveTopic(@Valid @ModelAttribute Topic topic) {
        topicService.save(topic);
        return "redirect:/topics";
    }

    @GetMapping("/{id}/delete")
    public String deleteTopic(@PathVariable Long id) {
        topicService.delete(id);
        return "redirect:/topics";
    }
}
