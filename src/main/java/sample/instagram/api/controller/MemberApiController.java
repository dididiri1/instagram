package sample.instagram.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sample.instagram.api.service.AuthService;
import sample.instagram.api.service.MemberService;
import sample.instagram.domain.member.Member;
import sample.instagram.dto.request.MemberCreateRequest;
import sample.instagram.dto.response.ResponseDto;

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
        Member member = memberService.createMember(request);

        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CREATED.value(), "회원가입 성공", null), HttpStatus.CREATED);
    }
}
