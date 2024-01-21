package sample.instagram.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.like.LikeRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.dto.member.response.MemberResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.instagram.domain.member.Role.ROLE_USER;

public class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private ImageRepositoryJpa imageRepositoryJpa;

    @Autowired
    private SubscribeRepositoryJpa subscribeRepositoryJpa;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        imageRepositoryJpa.deleteAllInBatch();
        subscribeRepositoryJpa.deleteAllInBatch();
        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("신규 회원을 등록 한다.")
    @Test
    void createMember() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("testUser1")
                .password(bCryptPasswordEncoder.encode("1234"))
                .email("test@example.com")
                .name("홍길동")
                .build();

        //when
        MemberResponse memberResponse = memberService.createMember(request);

        //then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getUsername()).isEqualTo("testUser1");
        assertThat(memberResponse.getEmail()).isEqualTo("test@example.com");
        assertThat(memberResponse.getName()).isEqualTo("홍길동");
    }

    @DisplayName("회원 정보를 조회 한다.")
    @Test
    void getMember() throws Exception {
        //given
        Member member = createMember("testUser","test@example.com", "홍길동");
        memberRepositoryJpa.save(member);

        //when
        MemberResponse memberResponse = memberService.getMember(member.getId());

        //then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getUsername()).isEqualTo("testUser");
        assertThat(memberResponse.getEmail()).isEqualTo("test@example.com");
        assertThat(memberResponse.getName()).isEqualTo("홍길동");
    }

    @DisplayName("회원을 수정 한다.")
    @Test
    void updateMember() throws Exception {
        //given
        Member member = createMember("testUser","test@example.com", "홍길동");
        memberRepositoryJpa.save(member);

        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .password(bCryptPasswordEncoder.encode("1234"))
                .email("test@gmail.com")
                .name("김구라")
                .build();

        //when
        MemberResponse memberResponse = memberService.updateMember(member.getId(), request);

        //then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo("test@gmail.com");
        assertThat(memberResponse.getName()).isEqualTo("김구라");
    }

    @DisplayName("회원 프로필을 조회 한다.")
    @Test
    void getMemberProfile() throws Exception {
        //given
        Member member = createMember("member1","test@example.com", "홍길동");
        Member saveMember = memberRepositoryJpa.save(member);

        Image image1 = createImage("caption1", saveMember);
        Image image2 = createImage("caption2", saveMember);
        imageRepositoryJpa.saveAll(List.of(image1, image2));

        //when
        MemberProfileResponse memberProfileResponse = memberService.getMemberProfile(member.getId(), member.getId());

        System.out.println("subscribeState= "+memberProfileResponse.isSubscribeState());

        //then
        assertThat(memberProfileResponse).isNotNull();
        assertThat(memberProfileResponse)
                .extracting("pageOwnerState", "imageCount", "subscribeState", "subscribeCount" , "name")
                .contains(true, 2, false, 0, "홍길동");

        assertThat(memberProfileResponse.getImages()).hasSize(2)
                .extracting("caption", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple("caption1", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png"),
                        tuple("caption2", "https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                );
    }

    private Member createMember(String username, String email, String name) {
        return Member.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode("1234"))
                .email(email)
                .name(name)
                .role(ROLE_USER)
                .build();
    }

    private Image createImage(String caption, Member member) {
        return Image.builder()
                .caption(caption)
                .imageUrl("https://s3.ap-northeast-2.amazonaws.com/kangmin-s3-bucket/example.png")
                .member(member)
                .build();
    }

}
