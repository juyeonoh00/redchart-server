package server.domain;

import jakarta.persistence.*;
import lombok.*;


import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Builder
@Getter@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 25, nullable = false)
    private String email;

    @Column(length = 25)
    private String password;

    @Column(length = 20, nullable = false)
    private String username;

    @Column(length = 40)
    private String introduction;

    @Column
    private String profileImage;

//    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
//    private Set<Follow> followers = new HashSet<>();
//
//    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY)
//    private Set<Follow> followings = new HashSet<>();
//
//    public User(String email, String username) {
//        this.email = email;
//        this.username = username;
    }

    //    public void passwordEncoding(PasswordEncoder passwordEncoder) {
//        password = passwordEncoder.encode(password);
//    }}

