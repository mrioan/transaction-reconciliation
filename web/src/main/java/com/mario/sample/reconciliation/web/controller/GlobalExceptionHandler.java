package com.mario.sample.reconciliation.web.controller;

import com.mario.sample.reconciliation.util.ExceptionUtils;
import com.mario.sample.reconciliation.web.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ModelAndView handleBadRequestException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildModelAndViewErrorPage(new ErrorResponse(status.value(), ex.getMessage()));
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ResponseBody
    public ModelAndView handleMultipartException(WebRequest request, MultipartException ex) {
        String bodyOfResponse;
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause != null) {
            bodyOfResponse = rootCause.getMessage();
        } else {
            bodyOfResponse = ex.getMessage();
        }
        HttpStatus status = HttpStatus.PAYLOAD_TOO_LARGE;
        return buildModelAndViewErrorPage(new ErrorResponse(status.value(), bodyOfResponse));
    }

    public static class ErrorResponse {
        private final int code;
        private final String message;

        ErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    private ModelAndView buildModelAndViewErrorPage(ErrorResponse errorResponse) {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("code", errorResponse.getCode());
        mav.addObject("message", errorResponse.getMessage());
        return mav;
    }

}