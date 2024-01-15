package sample.instagram.domain.image;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.QImageResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static sample.instagram.domain.image.QImage.image;
import static sample.instagram.domain.subscribe.QSubscribe.subscribe;

@Repository
public class ImageQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ImageQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<ImageResponse> getStoryImages(Long memberId, Pageable pageable) {
        List<Long> ids = getSubscribeMemberIds(memberId);

        QueryResults<ImageResponse> result = queryFactory
                .select(new QImageResponse(image.id, image.caption, image.imageUrl))
                .from(image)
                .where(image.member.id.in(ids))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ImageResponse> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, result.getTotal());
    }

    private List<Long> getSubscribeMemberIds(Long memberId) {
        return queryFactory
                .select(subscribe.toMember.id)
                .from(subscribe)
                .where(subscribe.fromMember.id.eq(memberId))
                .orderBy(subscribe.id.desc())
                .fetch();
    }
}
