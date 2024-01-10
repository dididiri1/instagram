package sample.instagram.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultStatus {

    SUCCESS("성공"),
    FAIL("실패");

    private final String text;
}

