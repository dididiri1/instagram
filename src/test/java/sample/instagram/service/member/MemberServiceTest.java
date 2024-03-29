package sample.instagram.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.request.ProfileImageResponse;
import sample.instagram.dto.member.response.*;
import sample.instagram.dto.member.request.ProfileImageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static sample.instagram.dto.Role.ROLE_USER;

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
                .username("test")
                .password(bCryptPasswordEncoder.encode("1234"))
                .email("test@example.com")
                .name("홍길동")
                .build();

        //when
        MemberCreateResponse response = memberService.createMember(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("test");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getName()).isEqualTo("홍길동");
    }

    @DisplayName("회원 정보를 조회 한다.")
    @Test
    void getMember() throws Exception {
        //given
        Member member = createMember("member1","test@example.com", "name1");
        memberRepositoryJpa.save(member);

        //when
        MemberResponse response = memberService.getMember(member.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("member1");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getName()).isEqualTo("name1");
    }

    @DisplayName("회원을 수정 한다.")
    @Test
    void updateMember() throws Exception {
        //given
        Member member = createMember("member1","test@naver.com", "홍길동");
        memberRepositoryJpa.save(member);

        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .password(bCryptPasswordEncoder.encode("1234"))
                .email("test@gmail.com")
                .name("김구라")
                .bio("자기소개")
                .build();

        //when
        MemberUpdateResponse response = memberService.updateMember(member.getId(), request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@gmail.com");
        assertThat(response.getName()).isEqualTo("김구라");
    }

    @DisplayName("회원 프로필을 조회 한다.")
    @Test
    void getMemberProfile() throws Exception {
        //given
        Member member = createMember("member1","test@example.com", "name1");
        Member saveMember = memberRepositoryJpa.save(member);

        Image image1 = createImage("caption1", saveMember);
        Image image2 = createImage("caption2", saveMember);
        imageRepositoryJpa.saveAll(List.of(image1, image2));

        //when
        MemberProfileResponse response = memberService.getMemberProfile(member.getId(), member.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response)
                .extracting("pageOwnerState", "imageCount", "subscribeState", "subscribeCount" , "name")
                .contains(true, 2, false, 0, "name1");

        assertThat(response.getImages()).hasSize(2)
                .extracting("caption", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple("caption1", "https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/test/sample.jpg"),
                        tuple("caption2", "https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/test/sample.jpg")
                );
    }

    @DisplayName("회원의 구독자 리스트를 조회한다.")
    @Test
    void getMemberSubscribes() throws Exception {
        //given
        Member member1 = createMember("member1", "test@example.com", "name1");
        Member member2 = createMember("member2", "test@example.com", "name2");
        Member member3 = createMember("member3", "test@example.com", "name3");
        memberRepositoryJpa.saveAll(List.of(member1, member2, member3));

        Subscribe subscribe1 = createSubscribe(member1, member2);
        Subscribe subscribe2 = createSubscribe(member2, member1);
        Subscribe subscribe3 = createSubscribe(member1, member3);
        Subscribe subscribe4 = createSubscribe(member3, member1);
        subscribeRepositoryJpa.saveAll(List.of(subscribe1, subscribe2, subscribe3, subscribe4));

        //when
        List<MemberSubscribeResponse> response = memberService.getMemberSubscribes(member1.getId(), member1.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2)
                .extracting("username", "subscribeState", "equalMemberState")
                .containsExactlyInAnyOrder(
                        tuple("member2", 1, 0),
                        tuple("member3", 1, 0)
                );
    }

    @DisplayName("회원 프로필 사진을 변경한다.")
    @Test
    void updateProfileImage() throws Exception {
        //given
        Member member1 = createMember("member1", "test@example.com", "name1");
        memberRepositoryJpa.save(member1);

        String profileImageUrl = "https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/profile/default.png";
        ProfileImageRequest request = ProfileImageRequest.builder()
                .memberId(member1.getId())
                .file(mock(MultipartFile.class))
                .build();

        // stubbing
        Mockito.when(s3UploaderService.uploadFileS3(any(), any(String.class)))
                .thenReturn(profileImageUrl);

        //when
        ProfileImageResponse response = memberService.updateProfileImage(request);

        //then
        assertThat(response.getProfileImageUrl()).isEqualTo(profileImageUrl);
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
                .imageUrl("https://kangmin-s3-bucket.s3.ap-northeast-2.amazonaws.com/storage/test/sample.jpg")
                .member(member)
                .build();
    }

    private Subscribe createSubscribe(Member fromMember, Member toMember) {
        return Subscribe.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
    }

}
