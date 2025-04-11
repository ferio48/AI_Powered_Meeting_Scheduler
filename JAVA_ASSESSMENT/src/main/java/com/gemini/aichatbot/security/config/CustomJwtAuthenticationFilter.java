package com.gemini.aichatbot.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.aichatbot.model.response.BasicRestResponse;
import com.gemini.aichatbot.constant.AppConstants;
import com.gemini.aichatbot.security.model.AuthenticationDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Custom JWT authentication filter that validates JWT tokens in each request.
 *
 * This filter runs once per request and performs the following:
 * - Extracts JWT from the Authorization header
 * - Validates the token
 * - Extracts user details and sets authentication in the security context
 * - Handles JWT-related exceptions with a custom JSON error response
 */
@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Provides an {@link ObjectMapper} for converting Java objects to JSON.
     *
     * @return a singleton {@link ObjectMapper}
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Filters each incoming request to validate the JWT token and set up the security context.
     *
     * @param request     the current HTTP request
     * @param response    the current HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if an internal error occurs
     * @throws IOException      if reading/writing the response fails
     */
    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String jwtToken = extractJwtFromRequest(request);
            if (StringUtils.hasText(jwtToken) && jwtUtil.validateToken(jwtToken)) {
                // Create Spring Security UserDetails from token
                final UserDetails userDetails = new User(
                        jwtUtil.getUsernameFromToken(jwtToken),
                        AppConstants.StringConstants.EMPTY,
                        jwtUtil.getRolesFromToken(jwtToken)
                );

                // Build authentication token and set additional details
                final UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                final AuthenticationDetails details = new AuthenticationDetails();
                details.setLoggedInUsername(jwtUtil.getUsernameFromToken(jwtToken));
                details.setLoggedInUserId(jwtUtil.getUserIdFromToken(jwtToken));
                details.setLoggedInUserRole(jwtUtil.getRoleFromToken(jwtToken));
                authenticationToken.setDetails(details);

                // Set authentication context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException | ExpiredJwtException | BadCredentialsException ex) {
            // Handle token errors by writing a structured JSON error response
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            BasicRestResponse restResponse = new BasicRestResponse();
            restResponse.setMessage(ex.getLocalizedMessage());
            restResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            restResponse.setTime(new Timestamp(System.currentTimeMillis()));

            String jsonResponse = objectMapper().writeValueAsString(restResponse);
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            return;
        }

        // Continue the request if no errors occurred
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header, if present.
     *
     * @param request the HTTP request
     * @return the token string, or {@code null} if not found
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
