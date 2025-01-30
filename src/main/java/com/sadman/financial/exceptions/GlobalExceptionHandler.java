package com.sadman.financial.exceptions;


import com.sadman.financial.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomMessageException.class)
    public ResponseEntity<JSONObject> handleCustomMessagePresentExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ResponseBuilder.error(null, ex.getMessage()).getJson(), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity<JSONObject> handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request, HttpServletResponse response) {
//        logger.error(exc.getMessage());
//        return new ResponseEntity<>(ResponseBuilder.error(null, "File size too large! Max size is " + exc.getMaxUploadSize()).getJson(), HttpStatus.BAD_REQUEST);
//    }
}

