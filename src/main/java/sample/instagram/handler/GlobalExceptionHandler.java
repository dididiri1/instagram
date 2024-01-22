package sample.instagram.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sample.instagram.dto.ApiResponse;
import sample.instagram.handler.ex.*;
import sample.instagram.util.Script;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public String validationException(CustomValidationException e) {
        // CMRespDto, Script 비교
        // 1. 클라이언트에게 응답할 때는 Script 좋음.
        // 2. Ajax 통신 - CMRespDto
        // 3. Android 통신 - CMRespDto

        if(e.getErrorMap() == null){
            return Script.back(e.getMessage());
        } else{
            return Script.back(e.getErrorMap().toString());
        }
    }

    @ExceptionHandler(CustomException.class)
    public String CustomException(CustomException e) {
        return Script.back(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object invalidRequestException(MethodArgumentNotValidException e) {
        Map<String,String> errorMap = new HashMap<>();
        for(FieldError error:e.getFieldErrors()){
            errorMap.put(error.getField(),error.getDefaultMessage());
        }

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.", errorMap), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ApiResponse<?>> apiException(CustomApiException e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiDuplicateKey.class)
    public ResponseEntity<ApiResponse<?>> apiDuplicateKeyException(CustomApiDuplicateKey e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomValidationApiException.class)
    public ResponseEntity<ApiResponse<?>> validationApiException(CustomValidationApiException e) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }
}
