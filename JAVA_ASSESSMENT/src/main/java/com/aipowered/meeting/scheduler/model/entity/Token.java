package com.aipowered.meeting.scheduler.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token_config")
public class Token extends Base {

    /**
     * The token value.
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * The refresh token value.
     */
    @Column(name = "refresh_token")
    private String refreshToken;

    /**
     * The expiration date and time of the token.
     */
    @Column(name = "expire_at")
    private Date accessTokenExpireAt;

    /**
     * The expiration date and time of the refresh token.
     */
    @Column(name = "refresh_token_expire_at")
    private Date refreshTokenExpireAt;

    /**
     * The user associated with the token.
     */
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}

