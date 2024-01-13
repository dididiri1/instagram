package sample.instagram.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.Role.ROLE_USER;

@Transactional
public class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepositoryJpa memberRepositoryJpa;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("신규 회원을 등록한다.")
    @Test
    void saveMember() throws Exception {
        //given
        Member member = createMember("testUser", "1234","test@naver.com", "홍길동");

        //when
        Member saveMember = memberRepositoryJpa.save(member);

        //then
        assertThat(saveMember).isNotNull();
        assertThat(saveMember.getUsername()).isEqualTo("testUser");
        assertThat(saveMember.getEmail()).isEqualTo("test@naver.com");
        assertThat(saveMember.getName()).isEqualTo("홍길동");
    }

    @DisplayName("회원 단건 정보를 조회한다.")
    @Test
    void findByMember() throws Exception {
        //given
        Member member = createMember("testUser", "1234","test@naver.com", "홍길동");
        memberRepositoryJpa.save(member);

        //when
        Member findMember = memberRepositoryJpa.findById(member.getId()).orElse(null);

        //then
        assertThat(findMember).isNotNull();
    }

    @DisplayName("회원을 수정 한다.")
    @Test
    void updateMember() throws Exception {
        //given
        Member member = createMember("testUser", "1234","test1@example.com", "홍길동");
        memberRepositoryJpa.save(member);

        //when
        Member findMember = memberRepositoryJpa.findById(member.getId()).orElse(null);
        findMember.setEmail("test2@example.com");
        findMember.setName("김구라");

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getEmail()).isEqualTo("test2@example.com");
        assertThat(findMember.getName()).isEqualTo("김구라");
    }

    private Member createMember(String username, String password, String email, String name) {
        return Member.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .name(name)
                .role(ROLE_USER)
                .build();
    }
}
