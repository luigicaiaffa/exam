package org.exam.java.exam.repository;

import java.util.Optional;

import org.exam.java.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);
}
