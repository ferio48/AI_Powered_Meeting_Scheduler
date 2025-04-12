package com.aipowered.meeting.scheduler.security.config;

import com.aipowered.meeting.scheduler.constant.AppConstants;
import com.aipowered.meeting.scheduler.exception.ResourceNotFoundException;
import com.aipowered.meeting.scheduler.model.dto.UserDto;
import com.aipowered.meeting.scheduler.model.entity.Token;
import com.aipowered.meeting.scheduler.model.entity.User;
import com.aipowered.meeting.scheduler.repositories.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for handling JSON Web Token (JWT) operations.
 *
 * Responsibilities:
 * - Generating access, refresh, and reset password tokens
 * - Validating tokens using secret key and database check
 * - Extracting information (username, user ID, roles, expiration) from tokens
 */
@Service
@Slf4j
public class JwtUtil {

    /** Repository to verify token existence and expiration in DB */
    @Autowired
    private TokenRepository tokenRepository;

    /** Base64-encoded secret key used for signing and verifying JWTs */
    @Value("${security.secret.key}")
    private String SECRET_KEY;

    /** Expiration time for access tokens in milliseconds */
    @Value("${security.accessToken.expiration.time}")
    private Long accessTokenExpirationTime;

    /** Expiration time for refresh tokens in milliseconds */
    @Value("${security.refreshToken.expiration.time}")
    private Long refreshTokenExpirationTime;

    /** Expiration time for reset password tokens in milliseconds */
    @Value("${security.resetPasswordToken.expiration.time}")
    private Long resetPasswordTokenExpirationTime;

    /**
     * Validates a JWT by checking its signature and expiration,
     * and confirming its presence in the token repository.
     *
     * @param jwtToken the token to validate
     * @return true if the token is valid and active
     */
    public boolean validateToken(String jwtToken) {
        return validateToken(jwtToken, SECRET_KEY) && isTokenValid(jwtToken);
    }

    /**
     * Checks if a token exists in the database and has not expired.
     *
     * @param jwtToken the token to check
     * @return true if token is valid and associated with a user
     * @throws ResourceNotFoundException if the token is not found
     */
    private boolean isTokenValid(String jwtToken) {
        final Token token = tokenRepository.findByAccessToken(jwtToken)
                .orElseThrow(() -> new ResourceNotFoundException("No Token found!!!"));

        if (new Date().before(token.getAccessTokenExpireAt())) {
            final User user = token.getUser();
            return user != null;
        }

        return false;
    }

    /**
     * Validates a JWT token using a secret key.
     *
     * @param jwtToken  the token to validate
     * @param secretKey the base64-encoded secret key
     * @return true if token is valid
     * @throws JwtException for any parsing/validation issues
     */
    private boolean validateToken(String jwtToken, String secretKey) {
        try {
            Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken);
            return true;
        } catch (MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException | ExpiredJwtException ex) {
            throw ex;
        }
    }

    /**
     * Retrieves the secret signing key from base64-encoded value.
     *
     * @return the secret signing {@link Key}
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (subject) from the JWT.
     *
     * @param jwtToken the token to parse
     * @return the username
     */
    public String getUsernameFromToken(String jwtToken) {
        log.info("Entered method to get username from the token");
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        return claims.getSubject();
    }

    /**
     * Extracts the list of roles from the token and maps them as Spring authorities.
     *
     * @param jwtToken the token to parse
     * @return list of {@link SimpleGrantedAuthority}
     */
    public List<SimpleGrantedAuthority> getRolesFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        final List<SimpleGrantedAuthority> roles = new ArrayList<>();
        final List<String> rolesFromToken = claims.get("roles", List.class);
        if (rolesFromToken != null) {
            rolesFromToken.forEach(roleFromToken -> roles.add(new SimpleGrantedAuthority(roleFromToken)));
        }

        return roles;
    }

    /**
     * Extracts the user ID from the JWT.
     *
     * @param jwtToken the token to parse
     * @return the user ID
     */
    public Integer getUserIdFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        return claims.get("id", Integer.class);
    }

    /**
     * Extracts the first role from the token's "roles" claim.
     * Useful when only one role is expected.
     *
     * @param jwtToken the token to parse
     * @return the role name, or null if no role found
     */
    public String getRoleFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        final List<String> rolesFromToken = claims.get("roles", List.class);
        if (!CollectionUtils.isEmpty(rolesFromToken)) {
            return rolesFromToken.get(0);
        }
        return null;
    }

    /**
     * Generates a JWT token for the given user and token type.
     *
     * @param userDetails user data including username, ID, and roles
     * @param tokenType   type of token: access, refresh, or reset
     * @return the signed JWT
     */
    public String generateToken(UserDto userDetails, String tokenType) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getRole().getAuthorities().stream()
                .map(role -> role.getAuthority()).collect(Collectors.toList()));
        claims.put("id", userDetails.getId());

        return doGenerateToken(claims, userDetails.getUsername(), tokenType);
    }

    /**
     * Internally generates a JWT based on claims, username, and token type.
     *
     * @param claims    claims to embed
     * @param username  subject of the token
     * @param tokenType type of token
     * @return JWT string
     */
    private String doGenerateToken(Map<String, Object> claims, String username, String tokenType) {
        long tokenExpirationTime;

        if (StringUtils.equals(tokenType, AppConstants.TokenType.ACCESS_TOKEN)) {
            tokenExpirationTime = accessTokenExpirationTime;
        } else if (StringUtils.equals(tokenType, AppConstants.TokenType.REFRESH_TOKEN)) {
            tokenExpirationTime = refreshTokenExpirationTime;
        } else {
            tokenExpirationTime = resetPasswordTokenExpirationTime;
        }

        return Jwts.builder()
                .setClaims(claims != null ? claims : new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    /**
     * Extracts the expiration time from the JWT.
     *
     * @param jwtToken the token to parse
     * @return expiration {@link Date}
     */
    public Date getExpireTimeFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        return claims.getExpiration();
    }
}
