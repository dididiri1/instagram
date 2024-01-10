package sample.instagram.domain.image;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.instagram.domain.BaseEntity;
import sample.instagram.domain.member.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;

    private String imageUrl;

    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = LAZY)
    private Member member;

    @Builder
    public Image(Long id, String caption, String imageUrl, Member member) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.member = member;
    }
}
