package sample.instagram.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberProfileRequest;
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
        Member memberEntity = memberRepositoryJpa.findByUsername(username);
        if (memberEntity != null) {
            throw new CustomApiDuplicateKey("이미 사용 중인 유저명입니다.");
        }
    }

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        String rawPassword = request.getPassword();
        String encPassowrd = bCryptPasswordEncoder.encode(rawPassword);
        Member member = request.toEntity(encPassowrd);
        Member memberEntity = memberRepositoryJpa.save(member);

        return MemberResponse.of(memberEntity);
    }

    public MemberResponse getMember(Long id) {
        Member memberEntity = memberRepositoryJpa.findById(id).get();
        return MemberResponse.of(memberEntity);
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {

        Member memberEntity = memberRepositoryJpa.findById(id)
                .orElseThrow(() -> new CustomApiException("해당 ID에 해당하는 회원을 찾을 수 없습니다."));

        memberEntity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        memberEntity.setName(request.getName());
        memberEntity.setEmail(request.getEmail());

        return MemberResponse.of(memberEntity);
    }

    public MemberProfileResponse getMemberProfile(Long pageMemberId, Long memberId) {

        MemberProfileResponse response = new MemberProfileResponse();

        Member memberEntity = memberRepositoryJpa.findById(pageMemberId).orElseThrow(() -> {
            throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
        });

        response.setMember(memberEntity);
        response.setPageOwnerState(pageMemberId == memberId); // 1은 페이지 주인, -1은 주인이 아님
        response.setImageCount(memberEntity.getImages().size());

        boolean subscribeState = subscribeRepositoryJpa.existsByFromMemberIdAndToMemberId(pageMemberId, memberId);

        int subscribeCount = subscribeRepositoryJpa.countByFromMemberId(pageMemberId);

        System.out.println("subscribeState = " + subscribeState);
        System.out.println("subscribeCount = " + subscribeCount);

        response.setSubscribeState(subscribeState);
        response.setSubscribeCount(subscribeCount);

        return response;
    }
}
