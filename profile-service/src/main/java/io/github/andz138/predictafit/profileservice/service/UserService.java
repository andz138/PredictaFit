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

        AppUser user = new AppUser();
        user.setEmail(request.email());
        user.setPasswordHash(request.plainPassword());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        AppUser savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }



    public UserResponse getUserById(String userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return toUserResponse(user);
    }

    private UserResponse toUserResponse(AppUser user) {
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
