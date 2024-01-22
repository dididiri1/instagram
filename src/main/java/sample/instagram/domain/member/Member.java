package sample.instagram.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import sample.instagram.domain.BaseEntity;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.subscribe.Subscribe;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50, unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String name;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "toMember", fetch = FetchType.LAZY)
    private List<Subscribe> subscribes = new ArrayList<>();

    @Builder
    private Member(Long id, String username, String password, String email, String name, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
