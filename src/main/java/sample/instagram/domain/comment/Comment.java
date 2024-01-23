package sample.instagram.domain.comment;

import lombok.Builder;
import lombok.Getter;
import sample.instagram.domain.BaseEntity;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.member.Member;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String content;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;

    @JoinColumn(name = "image_id")
    @ManyToOne(fetch = LAZY)
    private Image image;

    @Builder
    public Comment(Long id, String content, Member member, Image image) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.image = image;
    }
}
