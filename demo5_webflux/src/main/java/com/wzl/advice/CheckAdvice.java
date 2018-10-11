package com.wzl.advice;

import com.wzl.exceptions.CheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * 异常处理切面
 */
@ControllerAdvice
public class CheckAdvice {

    //处理的异常为 WebExchangeBindException
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity handlerBindException(WebExchangeBindException e) {
        return new ResponseEntity<String>(toStr(e), HttpStatus.BAD_REQUEST);
    }

    /**
     * 把校验异常转换成字符串
     * @param ex
     * @return
     */
    private String toStr(WebExchangeBindException ex) {
        return ex.getFieldErrors().stream().map(e -> e.getField() + ":" + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }



    @ExceptionHandler(CheckException.class)
    public ResponseEntity handlerCheckException(CheckException e) {
        return new ResponseEntity<String>(toStr2(e), HttpStatus.BAD_REQUEST);
    }



    private String toStr2(CheckException e) {
        return e.getFieldName() + ":错误的值" + e.getFieldValue();
    }

}
