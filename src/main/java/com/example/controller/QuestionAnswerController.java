package com.example.controller;

import com.example.model.QuestionAnswer;
import com.example.model.Topic;
import com.example.service.TopicService;
import com.example.service.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/topics/{id}/questions-answers")
public class QuestionAnswerController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private QuestionAnswerService questionAnswerService;

    @GetMapping()
    public String listQuestionAnswers(@PathVariable Long id, Model model) {
        Topic topic = topicService.findById(id);
        model.addAttribute("topic", topic);
        model.addAttribute("questionsAnswers", topic.getQuestionAnswers());
        return "questionsAnswers/questionsAnswers";
    }

    @GetMapping("/create")
    public String createQuestionAnswer(@PathVariable Long id, Model model) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setTopic(topicService.findById(id));
        model.addAttribute("questionAnswer", questionAnswer);
        return "questionsAnswers/createQuestionAnswer";
    }

    @PostMapping("/create")
    public String saveQuestionAnswer(@PathVariable Long id, @ModelAttribute QuestionAnswer questionAnswer) {
        questionAnswer.setTopic(topicService.findById(id));
        questionAnswerService.save(questionAnswer);
        return "redirect:/topics/" + id + "/questions-answers";
    }

    @GetMapping("/{questionAnswerId}")
    public String viewQuestionAnswer(@PathVariable Long id, @PathVariable Long questionAnswerId, Model model) {
        Topic topic = topicService.findById(id);
        model.addAttribute("topic", topic);
        QuestionAnswer questionAnswer = questionAnswerService.findById(questionAnswerId);
        model.addAttribute("questionAnswer", questionAnswer);
        return "questionsAnswers/viewQuestionAnswer";
    }

    @GetMapping("/{questionAnswerId}/delete")
    public String deleteQuestionAnswer(@PathVariable Long id,
                               @PathVariable Long questionAnswerId) {
        questionAnswerService.delete(questionAnswerId);
        return "redirect:/topics/" + id + "/questions-answers";
    }
}
