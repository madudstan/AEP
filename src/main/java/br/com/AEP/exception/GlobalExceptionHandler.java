package br.com.AEP.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AlimentoNotFoundException.class, InstituicaoNotFoundException.class,
            SolicitacaoNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRule(BusinessRuleException exception,
                                                        HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(),
                request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception,
                                                      HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, "Dados de entrada inválidos",
                request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido",
                request.getRequestURI(), Map.of());
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, String path,
                                                    Map<String, String> fieldErrors) {
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), message, path, fieldErrors);
        return ResponseEntity.status(status).body(error);
    }
}
