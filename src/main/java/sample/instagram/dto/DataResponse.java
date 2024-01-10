package sample.instagram.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DataResponse {

    private ResultStatus result;

    @Builder
    public DataResponse(ResultStatus result) {
        this.result = result;
    }
    public static DataResponse of(ResultStatus resultStatus) {
        return DataResponse.builder()
                .result(resultStatus)
                .build();
    }
}
