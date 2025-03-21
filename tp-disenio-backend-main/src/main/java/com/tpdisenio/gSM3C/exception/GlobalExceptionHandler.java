/**
 * GlobalExceptionHandler es una clase que maneja excepciones globales en la aplicación.
 * Utiliza las anotaciones @ControllerAdvice y @ExceptionHandler de Spring para interceptar
 * excepciones específicas y devolver respuestas HTTP adecuadas.
 * 
 * Esta clase actualmente maneja las siguientes excepciones:
 * - BedelAlreadyExistsException: Devuelve un estado HTTP 409 (CONFLICT) cuando se intenta
 *   crear un Bedel que ya existe.
 * - PasswordMismatchException: Devuelve un estado HTTP 400 (BAD REQUEST) cuando se
 *   encuentra un error de coincidencia de contraseñas.
 * - MethodArgumentNotValidException: Devuelve un estado HTTP 400 (BAD REQUEST) cuando se
 *   encuentra un error de validación en un DTO.
 * 
 * Se pueden agregar más métodos de manejo de excepciones personalizadas según sea necesario.
 */
package com.tpdisenio.gSM3C.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Solicitud no legible");
        response.put("message", "El cuerpo de la solicitud es inválido o está ausente.");
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BedelAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleBedelAlreadyExists(BedelAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UsuarioAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioAlreadyExist(UsuarioAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String, String>> handlePasswordMismatch(PasswordMismatchException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja excepciones de validación de campos.
     * Estas excepciones se lanzan cuando un campo de un DTO no cumple con las
     * restricciones
     * definidas en las anotaciones de validación de Spring (dentro del DTO).
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AdministradorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAdministradorNotFound(AdministradorNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BedelNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBedelNotFound(BedelNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AulaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAulaNotFound(AulaNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CuatrimestreNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCuatrimestreNotFound(CuatrimestreNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoAvailabilityException.class)
    public ResponseEntity<Map<String, String>> handleNoAvailability(NoAvailabilityException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicatedDayException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedDay(DuplicatedDayException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getClass().getSimpleName(), ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
