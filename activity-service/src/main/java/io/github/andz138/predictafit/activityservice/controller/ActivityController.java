package io.github.andz138.predictafit.activityservice.controller;

import io.github.andz138.predictafit.activityservice.dto.ActivityRequest;
import io.github.andz138.predictafit.activityservice.dto.ActivityResponse;
import io.github.andz138.predictafit.activityservice.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(
            @RequestHeader("X-User-ID") String userId,
            @Valid @RequestBody ActivityRequest request
    ) {
        return ResponseEntity.ok(activityService.createActivity(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getActivitiesForUser(
            @RequestHeader("X-User-ID") String userId
    ) {
        return ResponseEntity.ok(activityService.getActivitiesForUser(userId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(
            @PathVariable String activityId
    ) {
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}