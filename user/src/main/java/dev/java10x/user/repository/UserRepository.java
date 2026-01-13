package dev.java10x.user.repository;

import dev.java10x.user.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    List<UserModel> findByNameContainingIgnoreCase(String name);
    Optional<UserModel> findByEmail(String email);
}
