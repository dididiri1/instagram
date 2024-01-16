package sample.instagram.domain.image;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.image.reponse.QImageResponse;
import sample.instagram.dto.image.reponse.QImageStoryResponse;

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

    public List<ImageStoryResponse> getStoryImages(Long memberId, Pageable pageable) {
        List<Long> ids = getSubscribeMemberIds(memberId);

        QueryResults<ImageStoryResponse> result = queryFactory
                .select(new QImageStoryResponse(image.id, image.caption, image.imageUrl, member.username))
                .from(image)
                .join(image.member, member)
                .where(image.member.id.in(ids))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        /*List<ImageResponse> content = result.getResults();
        long total = result.getTotal();
        return new PageImpl<>(content, pageable, total);*/

        return result.getResults();
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
