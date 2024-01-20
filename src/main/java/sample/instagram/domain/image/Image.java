package sample.instagram.domain.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import sample.instagram.domain.BaseEntity;
import sample.instagram.domain.like.Like;
import sample.instagram.domain.member.Member;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "image")
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Image(Long id, String caption, String imageUrl, Member member) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.member = member;
    }
}
