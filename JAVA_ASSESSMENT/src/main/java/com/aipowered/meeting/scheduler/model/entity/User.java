package com.aipowered.meeting.scheduler.model.entity;

import com.aipowered.meeting.scheduler.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity class representing a user in the system.
 *
 * Inherits common fields (like ID, timestamps) from {@link Base}.
 * Stores user-specific attributes such as name, email, password, role, etc.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User extends Base {

    /**
     * Full name of the user.
     */
    @Column(name = "name")
    private String name;

    /**
     * Unique username used for login (may be email or custom).
     */
    @Column(name = "username")
    private String username;

    /**
     * Email address associated with the user.
     */
    @Column(name = "email_address")
    private String emailAddress;

    /**
     * Hashed password of the user.
     */
    @Column(name = "password")
    private String password;

    /**
     * Phone number of the user (used for verification or contact).
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Role of the user (e.g., ADMIN, USER), stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    /**
     * List of authentication tokens issued to the user.
     * Managed via a one-to-many relationship.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Token> tokenList;
}

