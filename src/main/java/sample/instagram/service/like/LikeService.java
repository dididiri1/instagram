package sample.instagram.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepository;
import sample.instagram.domain.like.Like;
import sample.instagram.domain.like.LikeRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.dto.DataResponse;
import sample.instagram.dto.ResultStatus;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepositoryJpa likeRepositoryJpa;

    private final MemberRepository memberRepository;

    private final ImageRepository imageRepository;

    public Like createLike(Long imageId, Long memberId) {
        Image image = imageRepository.findOne(imageId);
        Member member = memberRepository.findOne(memberId);
        Like like = Like.toEntity(image, member);
        Like saveLike = likeRepositoryJpa.save(like);

        return saveLike;
    }

    public DataResponse deleteLike(Long imageId, Long memberId) {
        likeRepositoryJpa.deleteByImageIdAndMemberId(imageId, memberId);
        return DataResponse.of(ResultStatus.SUCCESS);
    }
}
