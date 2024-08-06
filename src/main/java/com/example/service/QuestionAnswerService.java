package com.example.service;

import com.example.model.QuestionAnswer;
import com.example.repository.QuestionAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionAnswerService {

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    public QuestionAnswer findById(Long id) {
        return questionAnswerRepository.findById(id).orElse(null);
    }

    public QuestionAnswer save(QuestionAnswer questionAnswer) {
        return questionAnswerRepository.save(questionAnswer);
    }

    public void delete(Long id) {
        questionAnswerRepository.deleteById(id);
    }
}
