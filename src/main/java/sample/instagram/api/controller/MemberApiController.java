package sample.instagram.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.instagram.api.service.MemberService;
import sample.instagram.dto.ResponseDto;
import sample.instagram.dto.member.request.MemberCreateRequest;
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
    @GetMapping("/api/members/checkUsername/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        memberService.checkUsername(username);

        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "유저명 중복 체크 성공", null), HttpStatus.OK);
    }

    /**
     * @Method: createMember
     * @Description: 회원가입
     */
    @PostMapping("/api/members")
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberCreateRequest request) {
        MemberResponse memberResponse = memberService.createMember(request);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CREATED.value(), "회원가입 성공", memberResponse), HttpStatus.CREATED);
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<?> findMemberOne(@PathVariable("id") Long id) {
        MemberResponse memberResponse = memberService.findMemberOne(id);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK.value(), "회원 단건조회 성공", memberResponse), HttpStatus.OK);
    }
}
