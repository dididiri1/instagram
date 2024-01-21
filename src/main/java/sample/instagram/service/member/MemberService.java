package sample.instagram.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.dto.member.response.MemberResponse;
import sample.instagram.handler.ex.CustomApiDuplicateKey;
import sample.instagram.handler.ex.CustomApiException;
import sample.instagram.handler.ex.CustomException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepositoryJpa memberRepositoryJpa;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SubscribeRepositoryJpa subscribeRepositoryJpa;

    public void checkUsername(String username) {
        validateDuplicateUsername(username);
    }

    private void validateDuplicateUsername(String username) {
        Member memberEntity = memberRepositoryJpa.findByUsername(username);
        if (memberEntity != null) {
            throw new CustomApiDuplicateKey("이미 사용 중인 유저명입니다.");
        }
    }

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        String rawPassword = request.getPassword();
        Member member = request.toEntity(bCryptPasswordEncoder.encode(rawPassword));
        Member memberEntity = memberRepositoryJpa.save(member);

        return MemberResponse.of(memberEntity);
    }

    public MemberResponse getMember(Long id) {
        Member memberEntity = getMemberEntity(id);
        return MemberResponse.of(memberEntity);
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member memberEntity = getMemberEntity(id);
        memberEntity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        memberEntity.setName(request.getName());
        memberEntity.setEmail(request.getEmail());

        return MemberResponse.of(memberEntity);
    }

    private Member getMemberEntity(Long id) {
        return memberRepositoryJpa.findById(id)
                .orElseThrow(() -> new CustomApiException("해당 ID에 해당하는 회원을 찾을 수 없습니다."));
    }

    public MemberProfileResponse getMemberProfile(Long pageMemberId, Long memberId) {
        Member member = validateDuplicateMember(pageMemberId);
        //boolean subscribeState = isSubscribeState(pageMemberId, memberId);
        //int subscribeCount = getSubscribeCount(pageMemberId);
        //boolean pageOwnerState = isPageOwnerState(pageMemberId, memberId);

        return MemberProfileResponse.of(member, memberId);
    }

    private Member validateDuplicateMember(Long memberId) {
        Member findMember = memberRepositoryJpa.findById(memberId).orElseThrow(() -> {
            throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
        });

        return findMember;
    }

    private int getSubscribeCount(Long pageMemberId) {
        return subscribeRepositoryJpa.countByFromMemberId(pageMemberId);
    }

    private boolean isSubscribeState(Long pageMemberId, Long memberId) {
        return subscribeRepositoryJpa.existsByFromMemberIdAndToMemberId(pageMemberId, memberId);
    }


}
