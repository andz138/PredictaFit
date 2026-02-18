package io.github.andz138.predictafit.aiservice.messaging.listener;

import io.github.andz138.predictafit.aiservice.messaging.event.ActivityCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processActivity(ActivityCreatedEvent event) {
        log.info("""
                Received ActivityCreatedEvent
                activityId={}
                userId={}
                type={}
                duration={}
                """,
                event.activityId(),
                event.userId(),
                event.activityType(),
                event.durationMinutes()
        );
    }
}
