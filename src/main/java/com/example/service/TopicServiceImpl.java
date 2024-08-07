package com.example.service;

import com.example.dto.topic.TopicCreateDTO;
import com.example.dto.topic.TopicDTO;
import com.example.dto.topic.TopicUpdateDTO;
import com.example.model.Topic;
import com.example.repository.TopicRepository;
import com.example.service.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public List<TopicDTO> findAll() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map((topic) -> mapToTopicDTO(topic))
                .collect(Collectors.toList());
    }

    @Override
    public void save(TopicCreateDTO topicCreateDTO) {
        Topic topic = new Topic();
        topic.setTitle(topicCreateDTO.getTitle());
        topic.setQuestionAnswers(topicCreateDTO.getQuestionAnswers());
        topicRepository.save(topic);
    }

    @Override
    public Topic findById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Override
    public void updateTopic(Long id, TopicUpdateDTO topicUpdateDTO) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if (topic != null) {
            topic.setTitle(topicUpdateDTO.getTitle());
            topicRepository.save(topic);
        }
    }

    @Override
    public void delete(Long id) {
        topicRepository.deleteById(id);
    }

    private TopicDTO mapToTopicDTO(Topic topic){
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topic.getId());
        topicDTO.setTitle(topic.getTitle());
        topicDTO.setQuestionAnswers(topic.getQuestionAnswers());
        return topicDTO;
    }
}
