package com.example.controller;

import com.example.model.QuestionAnswer;
import com.example.model.Topic;
import com.example.service.TopicService;
import com.example.service.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String listQuestionAnswers(@PathVariable Long id, Model model) {
        Topic topic = topicService.findById(id);
        model.addAttribute("topic", topic);
        model.addAttribute("questionsAnswers", topic.getQuestionAnswers());
        return "questionsAnswers/questionsAnswers";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String createQuestionAnswer(@PathVariable Long id, Model model) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setTopic(topicService.findById(id));
        model.addAttribute("questionAnswer", questionAnswer);
        return "questionsAnswers/createQuestionAnswer";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String saveQuestionAnswer(@PathVariable Long id, @ModelAttribute QuestionAnswer questionAnswer) {
        questionAnswer.setTopic(topicService.findById(id));
        questionAnswerService.save(questionAnswer);
        return "redirect:/topics/" + id + "/questions-answers";
    }

    @GetMapping("/{questionAnswerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String viewQuestionAnswer(@PathVariable Long id, @PathVariable Long questionAnswerId, Model model) {
        Topic topic = topicService.findById(id);
        model.addAttribute("topic", topic);
        QuestionAnswer questionAnswer = questionAnswerService.findById(questionAnswerId);
        model.addAttribute("questionAnswer", questionAnswer);
        return "questionsAnswers/viewQuestionAnswer";
    }

    @GetMapping("/{questionAnswerId}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String editQuestionAnswer(@PathVariable Long id, @PathVariable Long questionAnswerId, Model model) {
        Topic topic = topicService.findById(id);
        model.addAttribute("topic", topic);
        QuestionAnswer questionAnswer = questionAnswerService.findById(questionAnswerId);
        model.addAttribute("questionAnswer", questionAnswer);
        return "questionsAnswers/editQuestionAnswer";
    }

    @PostMapping("/{questionAnswerId}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String updateQuestionAnswer(@PathVariable Long id, @PathVariable Long questionAnswerId, @ModelAttribute QuestionAnswer questionAnswer) {
        QuestionAnswer existingQuestionAnswer = questionAnswerService.findById(questionAnswerId);
        if (existingQuestionAnswer != null) {
            existingQuestionAnswer.setQuestion(questionAnswer.getQuestion());
            existingQuestionAnswer.setAnswer(questionAnswer.getAnswer()); // Пример других полей
            questionAnswerService.save(existingQuestionAnswer);
        }
        return "redirect:/topics/" + id + "/questions-answers";
    }

    @GetMapping("/{questionAnswerId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteQuestionAnswer(@PathVariable Long id,
                               @PathVariable Long questionAnswerId) {
        questionAnswerService.delete(questionAnswerId);
        return "redirect:/topics/" + id + "/questions-answers";
    }
}
