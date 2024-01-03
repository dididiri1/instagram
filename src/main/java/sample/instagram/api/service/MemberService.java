package sample.instagram.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.response.MemberResponse;
import sample.instagram.handler.ex.CustomApiDuplicateKey;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void checkUsername(String username) {
        Member memberEntity = memberRepository.findByUsername(username);
        if (memberEntity != null) {
            throw new CustomApiDuplicateKey("이미 사용 중인 유저명입니다.");
        }
    }

    public MemberResponse createMember(MemberCreateRequest request) {
        String rawPassword = request.getPassword();
        String encPassowrd = bCryptPasswordEncoder.encode(rawPassword);
        Member member = request.toEntity(encPassowrd);
        Member memberEntity = memberRepository.save(member);

        return MemberResponse.of(memberEntity);
    }


}
