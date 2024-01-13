package sample.instagram.domain.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import sample.instagram.domain.BaseEntity;
import sample.instagram.domain.member.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;

    private String imageUrl;

    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = LAZY)
    @JsonManagedReference // 정방향 참조를 무시
    private Member member;

    @Builder
    public Image(Long id, String caption, String imageUrl, Member member) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.member = member;
    }
}
