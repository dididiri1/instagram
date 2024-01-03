package sample.instagram.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_USER("일반회원"),
    ROLE_MANAGER("매니저"),
    ROLE_ADMIN("관리자");

    private final String text;
}

