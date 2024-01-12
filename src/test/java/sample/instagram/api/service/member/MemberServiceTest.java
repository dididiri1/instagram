package sample.instagram.api.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.dto.member.response.MemberResponse;
import sample.instagram.service.member.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.Role.ROLE_USER;

public class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    ImageRepositoryJpa imageRepositoryJpa;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {

        memberRepositoryJpa.deleteAllInBatch();
    }

    @DisplayName("신규 회원을 등록 한다.")
    @Test
    void createMember() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .username("testUser")
                .password(bCryptPasswordEncoder.encode("1234"))
                .email("test@naver.com")
                .name("홍길동")
                .build();

        //when
        MemberResponse memberResponse = memberService.createMember(request);

        //then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getUsername()).isEqualTo("testUser");
        assertThat(memberResponse.getEmail()).isEqualTo("test@naver.com");
        assertThat(memberResponse.getName()).isEqualTo("홍길동");
    }

    @DisplayName("회원 정보를 조회 한다.")
    @Test
    void getMember() throws Exception {
        //given
        Member member = createMember("testUser","test@naver.com", "홍길동");
        memberRepositoryJpa.save(member);

        //when
        MemberResponse memberResponse = memberService.getMember(member.getId());

        //then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getUsername()).isEqualTo("testUser");
        assertThat(memberResponse.getEmail()).isEqualTo("test@naver.com");
        assertThat(memberResponse.getName()).isEqualTo("홍길동");
    }

    @DisplayName("회원을 수정 한다.")
    @Test
    void updateMember() throws Exception {

        //given
        Member member = createMember("testUser","test@naver.com", "홍길동");
        memberRepositoryJpa.save(member);

        //when
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .password(bCryptPasswordEncoder.encode("1234"))
                .email("test@gmail.com")
                .name("김구라")
                .build();


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
        Long pageMemberId = 1L;
        Long memberId = 1L;

        Member member = createMember("testUser","test@naver.com", "홍길동");
        memberRepositoryJpa.save(member);

        //when
        MemberProfileResponse response = memberService.getMemberProfile(pageMemberId, memberId);

        //then
        assertThat(response).isNotNull();
        assertThat(response.isPageOwnerState()).isTrue();
        assertThat(response.isSubscribeState()).isFalse();
        assertThat(response.getImageCount()).isEqualTo(0);
        assertThat(response.getSubscribeCount()).isEqualTo(0);
        assertThat(response.getMember().getId()).isEqualTo(member.getId());
        assertThat(response.getMember().getUsername()).isEqualTo(member.getUsername());
        assertThat(response.getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getMember().getName()).isEqualTo(member.getName());

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

}
