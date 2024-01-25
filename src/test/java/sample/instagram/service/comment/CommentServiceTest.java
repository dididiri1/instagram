package sample.instagram.service.comment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.comment.Comment;
import sample.instagram.domain.comment.CommentRepositoryJpa;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.dto.comment.request.CommentRequest;
import sample.instagram.dto.comment.response.CommentResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.Role.ROLE_USER;

@Transactional
public class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private ImageRepositoryJpa imageRepositoryJpa;

    @AfterEach
    void tearDown() {
        commentRepositoryJpa.deleteAllInBatch();
        imageRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("댓글을 등록 한다.")
    @Test
    void createComment() throws Exception {
        //given
        Member member = createMember("member1", "name1");
        memberRepositoryJpa.save(member);

        Image image = createImage("caption1", member);
        imageRepositoryJpa.save(image);

        CommentRequest request = CommentRequest.builder()
                .memberId(member.getId())
                .imageId(image.getId())
                .content("댓글 내용입니다.")
                .build();

        //when
        CommentResponse commentResponse = commentService.createComment(request);

        //then
        Assertions.assertThat(commentResponse.getId()).isNotNull();
        assertThat(commentResponse)
                .extracting("id", "content", "username")
                .contains(commentResponse.getId(), "댓글 내용입니다.", "member1");
    }

    @DisplayName("댓글을 삭제 한다.")
    @Test
    void deleteComment() throws Exception {
        //given
        Member member = createMember("member1", "name1");
        memberRepositoryJpa.save(member);

        Image image = createImage("caption1", member);
        imageRepositoryJpa.save(image);

        Comment comment = createComment(member, image, "댓글 입니다.");
        Comment saveComment = commentRepositoryJpa.save(comment);

        //when
        commentService.deleteComment(saveComment.getId());

        //then
        Optional<Comment> findComment = commentRepositoryJpa.findById(saveComment.getId());
        assertThat(findComment.isPresent()).isFalse();

    }

    private Comment createComment(Member member, Image image, String content) {
        return Comment.builder()
                .member(member)
                .image(image)
                .content(content)
                .build();
    }

    private Member createMember(String username, String name) {
        return Member.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode("1234"))
                .email("test@example.com")
                .name(name)
                .role(ROLE_USER)
                .build();
    }

    private Image createImage(String caption, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example1.png")
                .member(member)
                .build();
    }
}
