package com.youwiz.demoapi.advice;

/*
ControllerAdvice는 Spring에서 제공하는 annotation으로 Controller에 전역에 적용되는 코드를 작성할 수 있게 해 줍니다.
또한 설정시 특정 패키지를 명시하면 적용되는 Controller의 범위도 제한할 수 있습니다.
이러한 특성을 이용하면 @ControllerAdvice와 @ExceptionHandler를 조합하여 예외 처리를 공통 코드로 분리하여 작성할 수 있습니다.
 */

import com.youwiz.demoapi.advice.exception.CUserNotFoundException;
import com.youwiz.demoapi.model.response.CommonResult;
import com.youwiz.demoapi.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestControllerAdvice   // Json 형태로 반환, 특정패키지만 적용하려면 @RestControllerAdvice(basePackages = "com.youwiz.api") 형식으로 적용.
public class ExceptionAdvice {
    private final ResponseService responseService;

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // 상태 여부
//    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
//        return responseService.getFailResult(); // 위에 상태여부를 지정했지만 다시 세팅하는 이유는 커스텀 메시지를 작성할 수 있게 하기 위해(예. 회원정보 없음 등등)
//    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // 상태 여부
    protected CommonResult userNotFound(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg")); // 위에 상태여부를 지정했지만 다시 세팅하는 이유는 커스텀 메시지를 작성할 수 있게 하기 위해(예. 회원정보 없음 등등)
    }

    // code 정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code 정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
