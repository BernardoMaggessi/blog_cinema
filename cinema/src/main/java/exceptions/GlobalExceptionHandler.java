package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Tratamento de exceção personalizada (Review não encontrada)
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<StandardError> handleReviewNotFound(ReviewNotFoundException ex) {
        StandardError error = new StandardError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Tratamento de exceção de validação (ConstraintViolationException)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> handleValidationException(ValidationException ex) {
        StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(),
                "Erro de validação: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Tratamento de exceções de validação no corpo da requisição
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extraindo todas as mensagens de erro de validação
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage("Erro de validação");

        // Adicionando os detalhes das mensagens de erro
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorResponse.addError(error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Tratamento de exceção genérica (erro interno no servidor)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGenericException(Exception ex) {
        StandardError error = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
