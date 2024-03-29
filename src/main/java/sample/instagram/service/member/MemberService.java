package sample.instagram.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberQueryRepository;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.request.ProfileImageResponse;
import sample.instagram.dto.member.response.*;
import sample.instagram.handler.ex.CustomApiDuplicateKey;
import sample.instagram.handler.ex.CustomException;
import sample.instagram.service.aws.S3UploadService;
import sample.instagram.dto.member.request.ProfileImageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepositoryJpa memberRepositoryJpa;

    private final MemberQueryRepository memberQueryRepository;

    private final SubscribeRepositoryJpa subscribeRepositoryJpa;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final S3UploadService s3UploaderService;

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
    public MemberCreateResponse createMember(MemberCreateRequest request) {
        validateDuplicateUsername(request.getUsername());
        Member member = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));
        Member memberEntity = memberRepositoryJpa.save(member);

        return MemberCreateResponse.of(memberEntity);
    }

    public MemberResponse getMember(Long id) {
        Member memberEntity = validateDuplicateMember(id);
        return MemberResponse.of(memberEntity);
    }

    @Transactional
    public MemberUpdateResponse updateMember(Long id, MemberUpdateRequest request) {
        Member memberEntity = validateDuplicateMember(id);
        if (request.getPassword() != null && !request.getPassword().equals("")) {
            memberEntity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        }
        if (request.getBio() != null && !request.getBio().equals("")) {
            memberEntity.setBio(request.getBio());
        }
        memberEntity.setName(request.getName());
        memberEntity.setEmail(request.getEmail());

        return MemberUpdateResponse.of(memberEntity);
    }

    public MemberProfileResponse getMemberProfile(Long pageMemberId, Long memberId) {
        Member member = validateDuplicateMember(pageMemberId);
        int subscribeCount = subscribeRepositoryJpa.countByFromMemberId(pageMemberId);

        return MemberProfileResponse.of(member, memberId, subscribeCount);
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
        Member findMember = validateDuplicateMember(request.getMemberId());
        String profileImageUrl = s3UploaderService.uploadFileS3(request.getFile(), "profile");
        findMember.setProfileImageUrl(profileImageUrl);

        return ProfileImageResponse.of(profileImageUrl);
    }
}
