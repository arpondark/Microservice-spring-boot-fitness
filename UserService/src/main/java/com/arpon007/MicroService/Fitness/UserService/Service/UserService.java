package com.arpon007.MicroService.Fitness.UserService.Service;

import org.springframework.stereotype.Service;

import com.arpon007.MicroService.Fitness.UserService.Repo.UserRepo;
import com.arpon007.MicroService.Fitness.UserService.dto.RegisterRequest;
import com.arpon007.MicroService.Fitness.UserService.dto.UserResponse;
import com.arpon007.MicroService.Fitness.UserService.models.User;
import com.arpon007.MicroService.Fitness.UserService.models.UserRole;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepo userRepo;

    public UserResponse register(RegisterRequest request) {
        if (userRepo.existsByEmail((request.getEmail()))) {
            User existingUser = userRepo.findByEmail(request.getEmail());
            UserResponse userResponse = new UserResponse();
            userResponse.setId(existingUser.getId());
            userResponse.setKeyloakId(existingUser.getKeyloakId());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());

        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setKeyloakId(request.getKeyloakId());
        // set password from the request (consider encoding in real apps)
        user.setPassword(request.getPassword());
        // explicitly set role to ensure it's persisted
        user.setRole(UserRole.USER);
        // persist user and get saved instance (with id, timestamps populated)
        User savedUser = userRepo.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setKeyloakId(savedUser.getKeyloakId());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        return userResponse;


    }

    public UserResponse getUserProfile(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setKeyloakId(user.getKeyloakId());
        userResponse.setPassword(user.getPassword());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }


    public Boolean existByUserId(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return userRepo.existsByKeyloakId(userId);
    }
}
