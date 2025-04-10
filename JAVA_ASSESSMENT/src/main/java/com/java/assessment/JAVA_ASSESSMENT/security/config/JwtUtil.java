package com.java.assessment.JAVA_ASSESSMENT.security.config;

import com.java.assessment.JAVA_ASSESSMENT.constant.AppConstants;
import com.java.assessment.JAVA_ASSESSMENT.exception.ResourceNotFoundException;
import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.Token;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import com.java.assessment.JAVA_ASSESSMENT.repositories.TokenRepository;
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

@Service
@Slf4j
public class JwtUtil {

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${security.secret.key}")
    private String SECRET_KEY;

    @Value("${security.accessToken.expiration.time}")
    private Long accessTokenExpirationTime;

    @Value("${security.refreshToken.expiration.time}")
    private Long refreshTokenExpirationTime;

    @Value("${security.resetPasswordToken.expiration.time}")
    private Long resetPasswordTokenExpirationTime;

    public boolean validateToken(String jwtToken) {
        return validateToken(jwtToken, SECRET_KEY) && isTokenValid(jwtToken);
    }

    private boolean isTokenValid(String jwtToken) {
        final Token token = tokenRepository.findByAccessToken(jwtToken)
                .orElseThrow(() -> new ResourceNotFoundException("No Token found!!!"));

        if(new Date().before(token.getAccessTokenExpireAt())) {
            final User user = token.getUser();
            return user != null;
        }

        return false;
    }

    private boolean validateToken(String jwtToken, String secretKey) {
        try {
            Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken);
            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
                 ExpiredJwtException ex) {
            throw ex;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String jwtToken) {
        log.info("Entered method to get username from the token");
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        return claims.getSubject();
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        final List<SimpleGrantedAuthority> roles = new ArrayList<>();
        final List<String> rolesFromToken = claims.get("roles", List.class);
        if(null != rolesFromToken) {
            rolesFromToken.forEach(roleFromToken -> roles.add(new SimpleGrantedAuthority(roleFromToken)));
        }

        return roles;
    }

    public Integer getUserIdFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        return claims.get("id", Integer.class);
    }

    public String getRoleFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        final List<String> rolesFromToken = claims.get("roles", List.class);
        if(CollectionUtils.isEmpty(rolesFromToken)) {
            return rolesFromToken.get(0);
        }

        return null;
    }

    public String generateToken(UserDto userDetails, String tokenType) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getRole().getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList()));
        claims.put("id", userDetails.getId());

        return doGenerateToken(claims, userDetails.getUsername(), tokenType);
    }

    private String doGenerateToken(Map<String, Object> claims, String username, String tokenType) {

        long tokenExpirationTime;

        if(StringUtils.equals(tokenType, AppConstants.TokenType.ACCESS_TOKEN)) {
            tokenExpirationTime = accessTokenExpirationTime;
        } else if(StringUtils.equals(tokenType, AppConstants.TokenType.REFRESH_TOKEN)) {
            tokenExpirationTime = refreshTokenExpirationTime;
        } else {
            tokenExpirationTime = resetPasswordTokenExpirationTime;
        }

        return Jwts
                .builder()
                .setClaims(claims != null ? claims : new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    public Date getExpireTimeFromToken(String jwtToken) {
        final Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(jwtToken).getBody();
        return claims.getExpiration();
    }
}
