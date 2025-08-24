package com.arpon007.MicroService.Fitness.UserService.Repo;

import com.arpon007.MicroService.Fitness.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
    boolean existsByEmail(String email);

   // Boolean existsByKeycloakId(String userId);

    User findByEmail(String email);
}
