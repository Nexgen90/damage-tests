package com.antkorvin.damagetests.api;

import com.antkorvin.damagetests.api.dto.ErrorDTO;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Created by Korovin Anatolii on 12.11.17.
 * <p>
 * Controller advice for exception handling in API's requests
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public
    @ResponseBody
    ErrorDTO processRuntimeException(RuntimeException exception) {
        return new ErrorDTO(0,
                            exception.getMessage(),
                            ExceptionUtils.getStackTrace(exception));
    }
}
