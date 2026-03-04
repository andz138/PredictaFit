package io.github.andz138.predictafit.profileservice.repository;

import io.github.andz138.predictafit.profileservice.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {

    boolean existsByKeycloakId(String keycloakId);

    Optional<AppUser> findByKeycloakId(String keycloakId);
}