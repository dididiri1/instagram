package sample.instagram.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.comment.Comment;
import sample.instagram.domain.comment.CommentRepositoryJpa;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepository;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.dto.comment.request.CommentRequest;
import sample.instagram.dto.comment.response.CommentResponse;

@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepositoryJpa commentRepositoryJpa;

    private final MemberRepository memberRepository;

    private final ImageRepository imageRepository;

    @Transactional
    public CommentResponse createComment(CommentRequest request) {
        Member member = memberRepository.findOne(request.getMemberId());
        Image image = imageRepository.findOne(request.getImageId());

        Comment comment = request.createComment(member, image);
        Comment saveComment = commentRepositoryJpa.save(comment);

        return CommentResponse.of(saveComment);
    }

}
