package aldra.api.adapter.web.controller;

import aldra.api.adapter.web.dto.ApplicationExceptionResponse;
import aldra.api.adapter.web.dto.ErrorCode;
import aldra.api.adapter.web.dto.ExceptionResponseBase;
import aldra.api.adapter.web.dto.ValidationExceptionResponse;
import aldra.common.framework.exception.ApplicationException;
import aldra.common.framework.exception.ValidationException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ApplicationExceptionResponse handleApplicationException(ApplicationException exception) {
    return ApplicationExceptionResponse.builder() //
        .error(ErrorCode.EAP0001_0001) //
        .message(exception.getMessage()) //
        .data(exception.getResponse()) //
        .build();
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ValidationExceptionResponse handleValidationException(ValidationException exception) {
    return ValidationExceptionResponse.builder() //
        .error(ErrorCode.EV0001_0001) //
        .messages(exception.getMessages()) //
        .build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public ExceptionResponseBase handleAccessDeniedException(AccessDeniedException exception) {
    return ExceptionResponseBase.builder() //
        .error(ErrorCode.EAZ0002_0001) //
        .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ApplicationExceptionResponse handleException(Exception exception) {
    return ApplicationExceptionResponse.builder() //
        .error(ErrorCode.ES0000_0000) //
        .message(exception.getMessage()) //
        .build();
  }
}
