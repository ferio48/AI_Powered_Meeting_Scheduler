package com.java.assessment.JAVA_ASSESSMENT.model.entity;

import com.java.assessment.JAVA_ASSESSMENT.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity class representing a User.
 * This class inherits common fields from the Base class and includes user-specific attributes.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User extends Base {

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Token> tokenList;
}

