package sample.instagram.controller.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.dto.subscribe.reponse.SubscribeMemberResponse;
import sample.instagram.service.image.ImageService;
import sample.instagram.service.image.reponse.ImagePopularResponse;
import sample.instagram.service.member.MemberService;
import sample.instagram.dto.ResponseDto;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberResponse;
import sample.instagram.service.subscribe.SubscribeService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    private final SubscribeService subscribeService;

    private final ImageService imageService;

    /**
     * @Method: checkUsername
     * @Description: 유저명 중복 체크
     */
    @GetMapping("/api/v1/members/checkUsername/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        memberService.checkUsername(username);

        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "유저명 중복 체크 성공", null), HttpStatus.OK);
    }

    /**
     * @Method: createMember
     * @Description: 회원 등록
     */
    @PostMapping("/api/v1/members/new")
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberCreateRequest request) {
        MemberResponse memberResponse = memberService.createMember(request);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CREATED.value(), "회원 등록 성공", memberResponse), HttpStatus.CREATED);
    }

    /**
     * @Method: findMemberOne
     * @Description: 회원 조회
     */
    @GetMapping("/api/v1/members/{id}")
    public ResponseEntity<?> getMember(@PathVariable("id") Long id) {
        MemberResponse memberResponse = memberService.getMember(id);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "회원 조회 성공", memberResponse), HttpStatus.OK);
    }

    /**
     * @Method: updateMember
     * @Description: 회원 수정
     */
    @PatchMapping("/api/v1/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id, @RequestBody MemberUpdateRequest request) {
        MemberResponse memberResponse = memberService.updateMember(id, request);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "회원 수정 성공", memberResponse), HttpStatus.OK);
    }

    /**
     * @Method: getMemberProfile
     * @Description: 회원 프로필 조회
     */
    @GetMapping("/api/v1/members/{pageMemberId}/profile/{id}")
    public ResponseEntity<?> getMemberProfile(@PathVariable("pageMemberId") Long pageMemberId, @PathVariable("id") Long id) {
        MemberProfileResponse response = memberService.getMemberProfile(pageMemberId, id);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "회원 프로필 조회 성공", response), HttpStatus.OK);
    }

    /**
     * @Method: getSubscribes
     * @Description: 회원 구독 정보 조회
     */
    @GetMapping("/api/v1/members/{pageMemberId}/subscribe/{id}")
    public ResponseEntity<?> getSubscribes(@PathVariable("pageMemberId") Long pageMemberId, @PathVariable("id") Long id) {
        List<SubscribeMemberResponse> subscribes = subscribeService.getSubscribes(pageMemberId, id);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "구독자 정보 리스트 조회 성공", subscribes), HttpStatus.OK);
    }

    /**
     * @Method: getStory
     * @Description: 회원 스토리 정보 조회
     */
    @GetMapping("/api/v1/members/{id}/story")
    public ResponseEntity<?> getStory(@PathVariable("id") Long memberId, @PageableDefault(size = 3) Pageable pageable) {
        List<ImageStoryResponse> imageResponses = imageService.getStory(memberId, pageable);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "스토리 조회 성공", imageResponses), HttpStatus.OK);
    }

}
