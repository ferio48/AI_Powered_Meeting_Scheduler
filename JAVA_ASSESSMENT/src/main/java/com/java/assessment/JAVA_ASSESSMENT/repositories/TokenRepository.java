package com.java.assessment.JAVA_ASSESSMENT.repositories;

import com.java.assessment.JAVA_ASSESSMENT.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for {@link Token}.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByAccessToken(String token);
}

