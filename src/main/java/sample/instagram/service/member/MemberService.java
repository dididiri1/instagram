package sample.instagram.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberQueryRepository;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.request.ProfileImageResponse;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.dto.member.response.MemberResponse;
import sample.instagram.dto.member.response.MemberSubscribeResponse;
import sample.instagram.handler.ex.CustomApiDuplicateKey;
import sample.instagram.handler.ex.CustomApiException;
import sample.instagram.handler.ex.CustomException;
import sample.instagram.service.aws.S3UploaderService;
import sample.instagram.dto.member.request.ProfileImageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepositoryJpa memberRepositoryJpa;

    private final MemberQueryRepository memberQueryRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final S3UploaderService s3UploaderService;

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
        Member memberEntity = findByMemberEntity(id);
        return MemberResponse.of(memberEntity);
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member memberEntity = findByMemberEntity(id);
        memberEntity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        memberEntity.setName(request.getName());
        memberEntity.setEmail(request.getEmail());

        return MemberResponse.of(memberEntity);
    }

    private Member findByMemberEntity(Long id) {
        return memberRepositoryJpa.findById(id)
                .orElseThrow(() -> new CustomApiException("해당 ID에 해당하는 회원을 찾을 수 없습니다."));
    }

    public MemberProfileResponse getMemberProfile(Long pageMemberId, Long memberId) {
        Member member = validateDuplicateMember(pageMemberId);
        return MemberProfileResponse.of(member, memberId);
    }

    private Member validateDuplicateMember(Long memberId) {
        Member findMember = memberRepositoryJpa.findById(memberId).orElseThrow(() -> {
            throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
        });

        return findMember;
    }
    public List<MemberSubscribeResponse> getMemberSubscribes(Long pageMemberId, Long memberId) {
        List<Member> members = memberQueryRepository.findAllWithSubscribe(pageMemberId);
        return members.stream()
                .map(member -> MemberSubscribeResponse.of(member, memberId))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProfileImageResponse updateProfileImage(ProfileImageRequest request) {
        Member findMember = findByMemberEntity(request.getMemberId());
        String profileImageUrl = s3UploaderService.uploadFileS3(request.getFile(), "profile");
        findMember.setProfileImageUrl(profileImageUrl);

        return ProfileImageResponse.of(profileImageUrl);
    }
}
