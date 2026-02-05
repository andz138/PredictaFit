package io.github.andz138.predictafit.profileservice.service;

import io.github.andz138.predictafit.profileservice.domain.AppUser;
import io.github.andz138.predictafit.profileservice.dto.RegisterRequest;
import io.github.andz138.predictafit.profileservice.dto.UserResponse;
import io.github.andz138.predictafit.profileservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        AppUser user = toAppUser(request);
        AppUser savedUser = userRepository.save(user);

        return toUserResponse(savedUser);
    }

    public UserResponse getUserById(String userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return toUserResponse(user);
    }

    private static AppUser toAppUser(RegisterRequest request) {
        return AppUser.builder()
                .email(request.email())
                .passwordHash(request.plainPassword()) // later: hash it
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    private static UserResponse toUserResponse(AppUser user) {
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
