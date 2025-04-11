package com.gemini.aichatbot.repositories;

import com.gemini.aichatbot.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for managing {@link Token} entities.
 *
 * Provides standard CRUD operations via {@link JpaRepository} and
 * additional methods for looking up tokens by their access token string.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    /**
     * Finds a token by its access token string.
     *
     * This is used to validate and fetch token details during authentication,
     * authorization, and token refresh processes.
     *
     * @param token the JWT access token string
     * @return an {@link Optional} containing the {@link Token} if found, or empty otherwise
     */
    Optional<Token> findByAccessToken(String token);
}

