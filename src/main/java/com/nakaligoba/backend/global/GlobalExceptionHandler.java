package com.nakaligoba.backend.global;

import com.nakaligoba.backend.exception.PermissionDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
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
    public ResponseEntity<ErrorResult> handlePermissionDeniedException(PermissionDeniedException ex) {
        log.warn("{} email: {}, nickname: {}", ex.getMessage(), ex.getDeniedMember().getEmail(), ex.getDeniedMember().getNickname());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResult(ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResult> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResult(ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest()
                .build();
    }
}
