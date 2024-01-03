package sample.instagram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDto<T> {

    private int status;
    private String message;
    private T data;

    public ResponseDto(int status){
        super();
        this.status = status;
    }
}
