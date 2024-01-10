package sample.instagram.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteResponse {

    private String result;

    @Builder
    public DeleteResponse(String result) {
        this.result = result;
    }
    public static DeleteResponse of() {
        return DeleteResponse.builder()
                .result("success")
                .build();
    }
}
