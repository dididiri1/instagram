package sample.instagram.domain.member;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;

    private String name;

    private String webSite;

    private String bio;

    private String email;

    private String gender;

    private String phone;

    private String profileImageUrl;

    private String role;
}
