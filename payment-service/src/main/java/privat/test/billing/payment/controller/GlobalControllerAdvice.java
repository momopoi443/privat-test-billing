package privat.test.billing.payment.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.*;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import privat.test.billing.payment.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static privat.test.billing.payment.filter.RequestIdFilter.REQUEST_ID_KEY;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto(MDC.get(REQUEST_ID_KEY), ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorDto(MDC.get(REQUEST_ID_KEY), errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorDto> handleConstraintViolation(
            ConstraintViolationException ex
    ) {
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorDto(MDC.get(REQUEST_ID_KEY), errors));
    }

    public record ValidationErrorDto (
            String rid,
            Map<String, String> validationErrors
    ) {}

    public record ErrorDto (
            String rid,
            String message
    ) {}
}