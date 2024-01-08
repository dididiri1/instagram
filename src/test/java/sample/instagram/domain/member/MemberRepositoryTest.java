package sample.instagram.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.IntegrationTestSupport;
import sample.instagram.dto.member.request.MemberCreateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.instagram.domain.member.Role.ROLE_USER;

@Transactional
public class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("신규 회원을 등록한다.")
    @Test
    void saveMember() throws Exception {
        //given
        Member member = createMember("testUser", "1234","test@naver.com", "홍길동");

        //when
        Member saveMember = memberRepository.save(member);

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
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(member.getId()).orElse(null);

        //then
        assertThat(findMember).isNotNull();
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
