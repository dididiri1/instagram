package sample.instagram.domain.subscribe;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.instagram.domain.BaseEntity;
import sample.instagram.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "subscribe_uk",
                        columnNames = {
                            "from_member_id","to_member_id"
                        }
                )
        }
)
public class Subscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "from_member_id")
    @ManyToOne(fetch = LAZY)
    private Member fromMember;

    @JoinColumn(name = "to_member_id")
    @ManyToOne(fetch = LAZY)
    private Member toMember;

    @Builder
    public Subscribe(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }

    public static Subscribe create(Member fromMember, Member toMember) {
        return Subscribe.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
    }
}
