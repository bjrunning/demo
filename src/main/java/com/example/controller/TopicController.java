package com.example.controller;

import com.example.dto.topic.TopicCreateDTO;
import com.example.dto.topic.TopicDTO;
import com.example.dto.topic.TopicUpdateDTO;
import com.example.model.Topic;
import com.example.service.interfaces.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String listTopics(Model model) {
        List<TopicDTO> topics = topicService.findAll();
        model.addAttribute("topics", topics);
        return "topics/topics";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String createTopic(Model model) {
        TopicCreateDTO topicCreateDTO = new TopicCreateDTO();
        model.addAttribute("topicCreateDTO", topicCreateDTO);
        return "topics/createTopic";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveTopic(@Valid @ModelAttribute("topicCreateDTO") TopicCreateDTO topicCreateDTO) {
        topicService.save(topicCreateDTO);
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
    public String updateTopic(@PathVariable Long id,
                              @Valid @ModelAttribute("topic") TopicUpdateDTO topicUpdateDTO) {
        topicService.updateTopic(id, topicUpdateDTO);
        return "redirect:/topics";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteTopic(@PathVariable Long id) {
        topicService.delete(id);
        return "redirect:/topics";
    }
}
