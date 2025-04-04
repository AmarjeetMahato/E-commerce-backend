package com.springboot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private String userId;

    @NotBlank(message = "firstname cannot be blank!")
    @Column(nullable = false)
    @Size(min = 2,max = 255)
    private String firstname;

    @NotBlank(message = "lastname cannot be blank!")
    @Column(nullable = false)
    @Size(min = 2,max = 255)
    private String lastname;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Email must be valid!")
    @Column(nullable = false, unique = true)
    private String email;


    @Min(value = 18, message = "Age must be at least 18!")
    @Column(nullable = false)
    private Integer age;

    @NotBlank(message = "Phone number cannot be blank!")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits!")
    @Column(nullable = false)
    private String number; // Changed to String to avoid issues with leading zeros

    @CreatedDate
    @Column(nullable = true, updatable = false)  // Ensure nullable = true
    private LocalDateTime createAt;

    @LastModifiedDate // Automatically updates on modification
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    // One User can have multiple Orders
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Order> orders = new ArrayList<>();

    // One User can have multiple Addresses
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference // Prevents infinite recursion
    private List<Address> addresses = new ArrayList<>();

    @NotNull(message = "Account locked status cannot be null")
    private Boolean accountLocked;

    @NotNull(message = "Enabled status cannot be null")
    private Boolean enabled;

    private int failedLoginAttempts = 0;  // Track login failures
    private LocalDateTime lockTime; // Track when the account was locked

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    // One User can have multiple Tokens
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        if (userId == null) {
            this.userId = UUID.randomUUID().toString();
        }
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r->new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return  this.email;
    }


    @Override
    public String getPassword() {
        return "";
    }


    @Override
    public String getUsername() {
        return  this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getFullName(){
        return  this.firstname + " " + this.lastname;
    }


}
