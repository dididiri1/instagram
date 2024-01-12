package sample.instagram.controller.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.instagram.dto.member.response.MemberProfileResponse;
import sample.instagram.service.member.MemberService;
import sample.instagram.dto.ResponseDto;
import sample.instagram.dto.member.request.MemberCreateRequest;
import sample.instagram.dto.member.request.MemberUpdateRequest;
import sample.instagram.dto.member.response.MemberResponse;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

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
    @PostMapping("/api/v1/members")
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

    @GetMapping("/api/v1/members/{id}/profile")
    public ResponseEntity<?> getMemberProfile(@PathVariable("id") Long memberId) {
        MemberProfileResponse response = memberService.getMemberProfile(1L, memberId);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "회원 프로필 조회 성공", response), HttpStatus.OK);
    }
}
