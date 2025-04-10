package com.java.assessment.JAVA_ASSESSMENT.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.assessment.JAVA_ASSESSMENT.constant.AppConstants;
import com.java.assessment.JAVA_ASSESSMENT.model.response.BasicRestResponse;
import com.java.assessment.JAVA_ASSESSMENT.security.model.AuthenticationDetails;
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

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String jwtToken = extractJwtFromRequest(request);
            if (StringUtils.hasText(jwtToken) && jwtUtil.validateToken(jwtToken)) {
                final UserDetails userDetails = new User(jwtUtil.getUsernameFromToken(jwtToken),
                        AppConstants.StringConstants.EMPTY, jwtUtil.getRolesFromToken(jwtToken));

                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                final AuthenticationDetails details = new AuthenticationDetails();
                details.setLoggedInUsername(jwtUtil.getUsernameFromToken(jwtToken));
                details.setLoggedInUserId(jwtUtil.getUserIdFromToken(jwtToken));
                details.setLoggedInUserRole(jwtUtil.getRoleFromToken(jwtToken));

                usernamePasswordAuthenticationToken.setDetails(details);

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException | BadCredentialsException ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Set the HTTP status code

            BasicRestResponse restResponse = new BasicRestResponse();
            restResponse.setMessage(ex.getLocalizedMessage());
            restResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            restResponse.setTime(new Timestamp(System.currentTimeMillis()));

            String jsonResponse = objectMapper().writeValueAsString(restResponse); // Convert the response object to JSON

            response.getWriter().write(jsonResponse); // Write the JSON response to the response body
            response.getWriter().flush();

            return; // Terminate the method execution
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
