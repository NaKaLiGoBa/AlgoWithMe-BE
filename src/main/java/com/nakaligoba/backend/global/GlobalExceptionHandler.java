package com.nakaligoba.backend.global;

import com.nakaligoba.backend.exception.PermissionDeniedException;
import com.nakaligoba.backend.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResult>> handleBindingError(MethodArgumentNotValidException ex) {
        List<ErrorResult> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> messageSource.getMessage(e, Locale.KOREAN))
                .map(ErrorResult::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<Void> handlePermissionDeniedException(PermissionDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
