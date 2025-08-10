package com.leonardoterrao.ordering.system.order.service.application.exception.handler;

import com.leonardoterrao.ordering.system.application.handler.ErrorDTO;
import com.leonardoterrao.ordering.system.application.handler.GlobalExceptionHandler;
import com.leonardoterrao.ordering.system.order.service.domain.exception.OrderDomainException;
import com.leonardoterrao.ordering.system.order.service.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OrderDomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(OrderDomainException exception) {
        log.error("An error occurred: {}", exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

    }

    @ResponseBody
    @ExceptionHandler(value = OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(OrderNotFoundException exception) {
        log.error("An error occurred: {}", exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .build();

    }



}
