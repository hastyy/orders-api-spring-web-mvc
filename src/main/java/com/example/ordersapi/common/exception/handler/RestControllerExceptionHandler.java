package com.example.ordersapi.common.exception.handler;

import com.example.ordersapi.common.exception.NotFoundException;
import com.example.ordersapi.common.exception.RestException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@NoArgsConstructor
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public void handleException(RestException ex, HttpServletResponse response) throws IOException {
        log.warn(ex.getMessage());
        response.sendError(ex.getHttpStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleParsingException(MethodArgumentTypeMismatchException ex,
                                       HttpServletResponse response) throws IOException {

        log.warn(ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), getParsingErrorMessage(ex));
    }

    private String getParsingErrorMessage(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == null)
            return String.format("%s is not valid", ex.getName());

        String[] requiredTypeParts = ex.getRequiredType().toString().split("\\.");
        String requiredType = requiredTypeParts[requiredTypeParts.length - 1].toLowerCase();

        return String.format("%s must be of type %s", ex.getName(), requiredType);
    }
}
