package server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Builder
@Getter@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 25, nullable = false)
    private String email;

    private String password;

    @Column(length = 20, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(length = 40)
    private String introduction;

    @Column
    private String profileImage;

    @Column
    private Long num_followers;
    @Column
    private Long num_followings;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY)
    private Set<Follow> followings = new HashSet<>();

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }
    @PrePersist
    @PreUpdate
    private void updateNumFollow() {
        this.num_followings = (followings != null) ? (long) followings.size() : 0L;
        this.num_followers = (followers != null) ? (long) followers.size() : 0L;
    }
    public void passwordEncoding(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

}