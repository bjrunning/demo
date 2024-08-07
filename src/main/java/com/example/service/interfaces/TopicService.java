package com.example.service.interfaces;

import com.example.dto.topic.TopicCreateDTO;
import com.example.dto.topic.TopicDTO;
import com.example.dto.topic.TopicUpdateDTO;
import com.example.model.Topic;

import java.util.List;

public interface TopicService {

    List<TopicDTO> findAll();

    void save(TopicCreateDTO topicCreateDTO);

    Topic findById(Long id);

    void updateTopic(Long id, TopicUpdateDTO topicUpdateDTO);

    void delete(Long id);
}
