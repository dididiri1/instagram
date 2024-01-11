package sample.instagram.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sample.instagram.dto.ResponseDto;
import sample.instagram.handler.ex.CustomApiDuplicateKey;
import sample.instagram.handler.ex.CustomApiException;
import sample.instagram.handler.ex.CustomValidationApiException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object invalidRequestException(MethodArgumentNotValidException e) {
        Map<String,String> errorMap = new HashMap<>();
        for(FieldError error:e.getFieldErrors()){
            errorMap.put(error.getField(),error.getDefaultMessage());
        }

        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.", errorMap), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ResponseDto<?>> apiException(CustomApiException e) {
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiDuplicateKey.class)
    public ResponseEntity<ResponseDto<?>> apiDuplicateKeyException(CustomApiDuplicateKey e) {
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CONFLICT.value(), e.getMessage(), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomValidationApiException.class)
    public ResponseEntity<ResponseDto<?>> validationApiException(CustomValidationApiException e) {
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }
}
