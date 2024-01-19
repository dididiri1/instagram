package sample.instagram.domain.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static sample.instagram.domain.image.QImage.image;
import static sample.instagram.domain.member.QMember.member;
import static sample.instagram.domain.subscribe.QSubscribe.subscribe;

@Repository
public class ImageQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ImageQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Image> findStoryWithImageMember(Long memberId, Pageable pageable) {
        List<Long> ids = queryFactory
                .select(subscribe.toMember.id)
                .from(subscribe)
                .where(subscribe.fromMember.id.eq(memberId))
                .fetch();

        System.out.println("ids = " + ids);

        List<Image> images = queryFactory
                .select(image)
                .from(image)
                .join(image.member, member).fetchJoin()
                .where(image.member.id.in(ids))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(image.id.desc())
                .fetch();

        return images;
    }
}
