package sample.instagram.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepository;
import sample.instagram.domain.like.Like;
import sample.instagram.domain.like.LikeRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepositoryJpa likeRepositoryJpa;

    private final MemberRepository memberRepository;

    private final ImageRepository imageRepository;

    @Transactional
    public Like createLike(Long imageId, Long memberId) {
        Image image = imageRepository.findOne(imageId);
        Member member = memberRepository.findOne(memberId);
        Like like = Like.toEntity(image, member);
        Like saveLike = likeRepositoryJpa.save(like);

        return saveLike;
    }

    @Transactional
    public void deleteLike(Long imageId, Long memberId) {
        likeRepositoryJpa.deleteByImageIdAndMemberId(imageId, memberId);
    }
}
