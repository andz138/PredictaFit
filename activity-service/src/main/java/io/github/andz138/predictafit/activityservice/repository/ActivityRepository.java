package io.github.andz138.predictafit.activityservice.repository;

import io.github.andz138.predictafit.activityservice.domain.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity, String> {

    List<Activity> findAllByUserId(String userId);
}