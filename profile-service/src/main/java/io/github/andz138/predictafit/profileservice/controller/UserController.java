package io.github.andz138.predictafit.profileservice.controller;

import io.github.andz138.predictafit.profileservice.dto.RegisterRequest;
import io.github.andz138.predictafit.profileservice.dto.UserResponse;
import io.github.andz138.predictafit.profileservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserResponse createdUser = userService.registerUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(createdUser.userId())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }
}
