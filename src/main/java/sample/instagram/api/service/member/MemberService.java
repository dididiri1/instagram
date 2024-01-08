package sample.instagram.api.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberResponse;
import sample.instagram.handler.ex.CustomApiDuplicateKey;
import sample.instagram.handler.ex.CustomApiException;

@Transactional(readOnly = true)
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

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        String rawPassword = request.getPassword();
        String encPassowrd = bCryptPasswordEncoder.encode(rawPassword);
        Member member = request.toEntity(encPassowrd);
        Member memberEntity = memberRepository.save(member);

        return MemberResponse.of(memberEntity);
    }

    public MemberResponse findMemberOne(Long id) {
        Member memberEntity = memberRepository.findById(id).get();
        return MemberResponse.of(memberEntity);
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {

        Member memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new CustomApiException("해당 ID에 해당하는 회원을 찾을 수 없습니다."));

        memberEntity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        memberEntity.setName(request.getName());
        memberEntity.setEmail(request.getEmail());

        return MemberResponse.of(memberEntity);
    }
}
