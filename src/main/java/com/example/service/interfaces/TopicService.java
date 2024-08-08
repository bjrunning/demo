package com.example.service.interfaces;

import com.example.dto.topic.TopicCreateDTO;
import com.example.dto.topic.TopicDTO;
import com.example.dto.topic.TopicUpdateDTO;
import com.example.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {

    Page<TopicDTO> findAll(Pageable pageable);

    Page<TopicDTO> searchByTitle(String title, Pageable pageable);

    void save(TopicCreateDTO topicCreateDTO);

    Topic findById(Long id);

    void updateTopic(Long id, TopicUpdateDTO topicUpdateDTO);

    void delete(Long id);
}
