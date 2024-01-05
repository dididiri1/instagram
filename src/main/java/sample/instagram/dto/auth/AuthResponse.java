package sample.instagram.dto.auth;

import lombok.Getter;

@Getter
public class AuthResponse {

    private String returnUrl;

    public AuthResponse(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
