package com.example.corporaterequests.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.corporaterequests.dto.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    /**
     * find by civil id.
     * @param civilId civil id
     * @return optional user
     */
    Optional<User> findByCivilId(String civilId);
}
