package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityMessageListener {

    private static final Logger log = LoggerFactory.getLogger(ActivityMessageListener.class);
    private final ActivityAiService aiService;
    private final RecommendationRepository repository;

    @RabbitListener(queues = "activity.queue", concurrency = "1")
    public void processActivity(Activity activity) {
        try {
            Recommendation recommendation = aiService.generateRecommendation(activity);
            repository.save(recommendation);
        } catch (Exception e) {
            log.error("AI processing failed, skipping message: ", e);
        }
    }

}
