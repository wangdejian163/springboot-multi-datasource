package com.bonc.casefile.policestation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author wangdejian
 * @since 2018/3/21
 */
@ControllerAdvice
public class GlobalExceptionHander {

    /**
     * 只要抛出该类型异常，则交付给该方法处理.
     *
     * @param request   request
     * @param exception exception
     * @return 异常信息
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleNotFound(HttpServletRequest request, NotFoundException exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(HttpStatus.NOT_FOUND.toString());
        errorInfo.setMessage(exception.getMessage());
        ResponseEntity<ErrorInfo> responseEntity = new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.NOT_FOUND);

        return responseEntity;
    }

}
