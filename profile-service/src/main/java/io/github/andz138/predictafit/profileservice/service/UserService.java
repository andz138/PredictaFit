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
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPlainPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        AppUser savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }



    public UserResponse getUserById(String userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return toUserResponse(user);
    }

    private UserResponse toUserResponse(AppUser user) {
        UserResponse dto = new UserResponse();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

}
