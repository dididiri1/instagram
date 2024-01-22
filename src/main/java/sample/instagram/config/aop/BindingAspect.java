package sample.instagram.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import sample.instagram.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class BindingAspect {

    @Around("execution(* sample.instagram.controller.api.image.ImageApiController.*(..))")
    public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        //String method = proceedingJoinPoint.getSignature().getName();

        System.out.println("AOP Aspect validCheck is invoked.");

        Object[] args = proceedingJoinPoint.getArgs();

        for (Object arg : args) {
            System.out.println("Argument: " + arg);
        }

        for (Object arg:args){
            if(arg instanceof BindingResult){
                BindingResult bindingResult = (BindingResult) arg;
                if(bindingResult.hasErrors()){
                    Map<String,String> errorMap = new HashMap<>();
                    for(FieldError error:bindingResult.getFieldErrors()){
                        errorMap.put(error.getField(),error.getDefaultMessage());
                    }
                    return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.", errorMap), HttpStatus.BAD_REQUEST);
                }
            }
        }

        return proceedingJoinPoint.proceed(); // 함수의 스택을 실행해라
    }
}
