package org.cayman.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    String defaultErrorHandler(Exception e) throws Exception {
        log.warn("Exception: ", e);
        if (e instanceof AccessDeniedException) {
            return "redirect:/accessDenied";
        } else {
            return "redirect:/error";
        }
    }
}
