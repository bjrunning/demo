package com.example.dto.topic;

import com.example.model.QuestionAnswer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopicDTO {

    private Long id;

    private String title;

    private List<QuestionAnswer> questionAnswers;
}
