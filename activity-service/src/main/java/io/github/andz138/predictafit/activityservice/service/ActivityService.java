package io.github.andz138.predictafit.activityservice.service;

import io.github.andz138.predictafit.activityservice.domain.Activity;
import io.github.andz138.predictafit.activityservice.dto.ActivityRequest;
import io.github.andz138.predictafit.activityservice.dto.ActivityResponse;
import io.github.andz138.predictafit.activityservice.messaging.event.ActivityCreatedEvent;
import io.github.andz138.predictafit.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse createActivity(String userId, ActivityRequest request) {
        userValidationService.assertUserExists(userId);

        Activity savedActivity = activityRepository.save(toActivity(userId, request));
        ActivityCreatedEvent event = toCreatedEvent(savedActivity);

        publishActivityCreated(event);

        return toResponse(savedActivity);
    }

    public List<ActivityResponse> getActivitiesForUser(String userId) {
        return activityRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ActivityResponse getActivityById(String activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Activity not found: " + activityId)
                );

        return toResponse(activity);
    }

    private void publishActivityCreated(ActivityCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            log.debug("Published ActivityCreatedEvent activityId={} to exchange={} routingKey={}",
                    event.activityId(), exchange, routingKey);
        } catch (Exception ex) {
            log.error("Failed to publish ActivityCreatedEvent activityId={} (exchange={}, routingKey={})",
                    event.activityId(), exchange, routingKey, ex);
        }
    }

    private Activity toActivity(String userId, ActivityRequest request) {
        return Activity.builder()
                .userId(userId)
                .activityType(request.activityType())
                .durationMinutes(request.durationMinutes())
                .caloriesBurned(request.caloriesBurned())
                .startedAt(request.startedAt())
                .metrics(request.metrics())
                .build();
    }

    private ActivityResponse toResponse(Activity activity) {
        return new ActivityResponse(
                activity.getActivityId(),
                activity.getUserId(),
                activity.getActivityType(),
                activity.getDurationMinutes(),
                activity.getCaloriesBurned(),
                activity.getStartedAt(),
                activity.getMetrics(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }

    private ActivityCreatedEvent toCreatedEvent(Activity savedActivity) {
        return new ActivityCreatedEvent(
                savedActivity.getActivityId(),
                savedActivity.getUserId(),
                savedActivity.getActivityType().name(),
                savedActivity.getDurationMinutes(),
                savedActivity.getCaloriesBurned(),
                savedActivity.getStartedAt()
        );
    }
}