package sample.instagram.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.dto.member.request.MemberCreateRequest;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member createMember(MemberCreateRequest request) {
        String rawPassword = request.getPassword();
        String encPassowrd = bCryptPasswordEncoder.encode(rawPassword);
        Member member = request.toEntity(encPassowrd);

        Member memberEntity = memberRepository.save(member);

        return memberEntity;
    }
}
