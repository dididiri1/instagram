package sample.instagram.controller.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sample.instagram.dto.ApiResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.member.request.ProfileImageResponse;
import sample.instagram.dto.member.response.*;
import sample.instagram.service.image.ImageService;
import sample.instagram.service.member.MemberService;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.request.ProfileImageRequest;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    private final ImageService imageService;

    /**
     * @Method: checkUsername
     * @Description: 유저명 중복 체크
     */
    @GetMapping("/api/v1/members/checkUsername/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        memberService.checkUsername(username);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "유저명 중복 체크 성공", null), HttpStatus.OK);
    }

    /**
     * @Method: createMember
     * @Description: 회원 등록
     */
    @PostMapping("/api/v1/members/new")
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberCreateRequest request) {
        MemberCreateResponse response = memberService.createMember(request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "회원 등록 성공", response), HttpStatus.CREATED);
    }

    /**
     * @Method: findMemberOne
     * @Description: 회원 조회
     */
    @GetMapping("/api/v1/members/{id}")
    public ResponseEntity<?> getMember(@PathVariable("id") Long id) {
        MemberResponse response = memberService.getMember(id);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "회원 조회 성공", response), HttpStatus.OK);
    }

    /**
     * @Method: updateMember
     * @Description: 회원 수정
     */
    @PatchMapping("/api/v1/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id, @RequestBody MemberUpdateRequest request) {
        MemberUpdateResponse response = memberService.updateMember(id, request);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "회원 수정 성공", response), HttpStatus.OK);
    }

    /**
     * @Method: getMemberProfile
     * @Description: 회원 프로필 조회
     */
    @GetMapping("/api/v1/members/{pageMemberId}/profile/{id}")
    public ResponseEntity<?> getMemberProfile(@PathVariable("pageMemberId") Long pageMemberId, @PathVariable("id") Long id) {
        MemberProfileResponse response = memberService.getMemberProfile(pageMemberId, id);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "회원 프로필 조회 성공", response), HttpStatus.OK);
    }

    /**
     * @Method: getSubscribes
     * @Description: 회원 구독 정보 조회
     */
    @GetMapping("/api/v1/members/{pageMemberId}/subscribe/{id}")
    public ResponseEntity<?> getSubscribes(@PathVariable("pageMemberId") Long pageMemberId, @PathVariable("id") Long id) {
        List<MemberSubscribeResponse> subscribes = memberService.getMemberSubscribes(pageMemberId, id);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "구독자 조회 성공", subscribes), HttpStatus.OK);
    }

    /**
     * @Method: getStory
     * @Description: 회원 구독자 스토리 정보 조회
     */
    @GetMapping("/api/v1/members/{id}/subscribe/story")
    public ResponseEntity<?> getMyStory(@PathVariable("id") Long memberId, @PageableDefault(size = 3) Pageable pageable) {
        List<ImageStoryResponse> response = imageService.getMyStory(memberId, pageable);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "구독자 스토리 조회 성공", response), HttpStatus.OK);
    }

    /**
     * @Method: getStory
     * @Description: 회원 나의 스토리 정보 조회
     */
    @GetMapping("/api/v1/members/{id}/story")
    public ResponseEntity<?> getStory(@PathVariable("id") Long memberId, @PageableDefault(size = 3) Pageable pageable) {
        List<ImageStoryResponse> response = imageService.getStory(memberId, pageable);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "스토리 조회 성공", response), HttpStatus.OK);
    }

    /**
     * @Method: updateProfileImage
     * @Description: 회원 프로필사진 변경
     */
    @PostMapping("/api/v1/members/profileImage")
    public ResponseEntity<?> updateProfileImage(@Valid @ModelAttribute ProfileImageRequest request, BindingResult bindingResult) {
        if(request.getFile().isEmpty()) {
            throw new ValidationException("이미지가 첨부되지 않았습니다.");
        }
        ProfileImageResponse response = memberService.updateProfileImage(request);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "프로필사진 변경 성공", response), HttpStatus.OK);
    }

}
