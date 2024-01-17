package sample.instagram.domain.like;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.instagram.domain.BaseEntity;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.member.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "likes_uk",
                        columnNames = {"image_id", "member_id"}
                )
        })
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Like(Image image, Member member) {
        this.image = image;
        this.member = member;
    }

    public static Like toEntity(Image image, Member member) {
        return Like.builder()
                .image(image)
                .member(member)
                .build();
    }
}
