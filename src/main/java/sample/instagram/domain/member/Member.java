package sample.instagram.domain.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(length = 50)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String name;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // 역방향 참조를 무시
    private List<Image> images = new ArrayList<>(); // 양방향 매핑

    @OneToMany(mappedBy = "toMember")
    @JsonBackReference
    private List<Subscribe> subscribes = new ArrayList<>(); // 양방향 매핑

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
