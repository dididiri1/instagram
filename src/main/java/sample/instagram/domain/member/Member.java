package sample.instagram.domain.member;

import lombok.*;
import sample.instagram.domain.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50, unique = true)
    private String username;

    private String password;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(Long id, String username, String password, String email, String name, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
