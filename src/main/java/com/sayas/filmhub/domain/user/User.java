package com.sayas.filmhub.domain.user;

import com.sayas.filmhub.domain.comment.Comment;
import com.sayas.filmhub.domain.errorreport.ErrorReport;
import com.sayas.filmhub.domain.rating.Rating;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "shadow_banned", nullable = false)
    private boolean shadowBanned;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<UserRole> roles = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Rating> ratings;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ErrorReport> reports;
    public boolean isShadowBanned() {
        return shadowBanned;
    }

    public void setShadowBanned(boolean shadowBanned) {
        this.shadowBanned = shadowBanned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

}