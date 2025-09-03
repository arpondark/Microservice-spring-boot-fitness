package com.arpon007.MicroService.Fitness.UserService.Repo;

import com.arpon007.MicroService.Fitness.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    Boolean existsByEmail(String email);

    User findByEmail(String email);

    Boolean existsByKeyloakId(String userId);
    
    java.util.Optional<User> findByKeyloakId(String keycloakId);
}
