package sn.ept.git.seminaire.cicd.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.models.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ISENE
 */
@Slf4j
@RestControllerAdvice
public class TodoExceptionHandler {

    public static final String TEMPLATE_LOG_MESSAGE = "Une exception s'est produite. Methoode capturant = {}, claase = {}";
    public static final String TEMPLATE_CHAMP_EN_ERREUR = "Champ = %s. Cause = %s";

    @ExceptionHandler(value = {ItemNotFoundException.class})
    protected ResponseEntity<ErrorModel> notFound(
            ItemNotFoundException exception, WebRequest request) {
        log.info(TEMPLATE_LOG_MESSAGE, "notFound", exception.getClass().getName());
        return buildResponse(HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(value = {ItemExistsException.class})
    protected ResponseEntity<ErrorModel> conflict(
            ItemExistsException exception, WebRequest request) {
        log.info(TEMPLATE_LOG_MESSAGE, "conflict", exception.getClass().getName());
        return buildResponse(HttpStatus.CONFLICT, exception, request);
    }

    @ExceptionHandler(value = {ValidationException.class,})
    protected ResponseEntity<ErrorModel> violation(
            ValidationException exception, WebRequest request) {
        log.info(TEMPLATE_LOG_MESSAGE, "violation", exception.getClass().getName());
        return buildResponse(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class,})
    protected ResponseEntity<ErrorModel> invalideArgument(
            MethodArgumentNotValidException exception, WebRequest request) {
        log.info(TEMPLATE_LOG_MESSAGE, "invalideArgument", exception.getClass().getName());
        String message = exception
                .getAllErrors()
                .stream()
                .map(error->{
                   if(error instanceof FieldError fieldError){
                       return TEMPLATE_CHAMP_EN_ERREUR.formatted(fieldError.getField(),fieldError.getDefaultMessage());
                   }
                    return error.getDefaultMessage();
                })
                .toList().toString()
                .replace("[","")
                .replace("]","");

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }


    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorModel> othersExceptions(
            Exception exception, WebRequest request) {
        log.info(TEMPLATE_LOG_MESSAGE, "othersExceptions", exception.getClass().getName());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<ErrorModel> responseStatusException(
            ResponseStatusException exception, WebRequest request) {
        log.info(TEMPLATE_LOG_MESSAGE, "responseStatusException", exception.getClass().getName());
        return buildResponse(HttpStatus.resolve(exception.getStatusCode().value()), exception, request);
    }

    private static ResponseEntity<ErrorModel> buildResponse(final HttpStatus status, final Exception exception, final WebRequest request) {
        return buildResponse(status, exception.getMessage(), request);
    }

    private static ResponseEntity<ErrorModel> buildResponse(final HttpStatus status, final String message, final WebRequest request) {
        HttpStatus finalStatus = Optional.ofNullable(status).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorModel error = ErrorModel.builder()
                .status(finalStatus.value())
                .date(LocalDateTime.now(Clock.systemUTC()))
                .message(message)
                .url(request.getDescription(true))
                .build();
        return ResponseEntity.status(finalStatus).body(error);
    }

}