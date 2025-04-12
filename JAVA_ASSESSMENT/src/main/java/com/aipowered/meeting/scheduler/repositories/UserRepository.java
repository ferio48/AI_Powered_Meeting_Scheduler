package com.aipowered.meeting.scheduler.repositories;

import com.aipowered.meeting.scheduler.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 *
 * Extends {@link JpaRepository} to provide CRUD operations and additional
 * methods for querying users by email, phone number, or username.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their email address.
     *
     * @param email the user's email address
     * @return an {@link Optional} containing the {@link User} if found, or empty otherwise
     */
    Optional<User> findByEmailAddress(String email);

    /**
     * Finds a user by their phone number (such as one verified via Twilio).
     *
     * @param twilioVerifyPhoneNumber the user's verified phone number
     * @return an {@link Optional} containing the {@link User} if found, or empty otherwise
     */
    Optional<User> findByPhoneNumber(String twilioVerifyPhoneNumber);

    /**
     * Finds a user by their username (could be an email or unique login identifier).
     *
     * @param username the username of the user
     * @return an {@link Optional} containing the {@link User} if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);
}
