package com.example.controller;

import com.example.model.Topic;
import com.example.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String listTopics(Model model) {
        model.addAttribute("topics", topicService.findAll());
        return "topics/topics";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String createTopic(Model model) {
        model.addAttribute("topic", new Topic());
        return "topics/createTopic";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveTopic(@Valid @ModelAttribute Topic topic) {
        topicService.save(topic);
        return "redirect:/topics";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String editTopic(@PathVariable Long id, Model model) {
        Topic topic = topicService.findById(id);
        model.addAttribute("topic", topic);
        return "topics/editTopic";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String updateTopic(@PathVariable Long id, @Valid @ModelAttribute Topic topic) {
        Topic existingTopic = topicService.findById(id);
        if (existingTopic != null) {
            existingTopic.setTitle(topic.getTitle());
            topicService.save(existingTopic);
        }
        return "redirect:/topics";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteTopic(@PathVariable Long id) {
        topicService.delete(id);
        return "redirect:/topics";
    }
}
