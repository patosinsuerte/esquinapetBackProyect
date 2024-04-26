package com.esquinaPet.veterinariabackend.infra.errors;


import com.esquinaPet.veterinariabackend.dto.ApiError;
import com.esquinaPet.veterinariabackend.infra.exceptions.InvalidAppointmentDateException;
import com.esquinaPet.veterinariabackend.infra.exceptions.InvalidAppointmentTimeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    // error 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> error404Handler(EntityNotFoundException exception) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Error: ", "Entity not found");
        responseBody.put("Message: ", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }


    // error 400 bad rquest
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity error400Handler(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors().stream().map(ErrorValidation400::new).toList();
        System.out.println(exception.getAllErrors().stream().map(each -> each.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }


    // record
    private record ErrorValidation400(String field, String error) {
        public ErrorValidation400(FieldError error) {
            this(error.getField(), getErrorMessage(error));
        }

        private static String getErrorMessage(FieldError error) {
            switch (error.getField()) {
                case "phone":
                    return "El número de teléfono debe tener 9 dígitos.";
                case "repeatedPassword":
                    return "La confirmación de contraseña no puede estar vacía.";
                case "password":
                    return "La contraseña debe tener al menos 6 caracteres, incluyendo al menos una letra mayúscula y un número.";
                case "rut":
                    return "Ingresa un rut valido porfavor.";
                case "name":
                    return "Ingresa un nombre valido porfavor.";
                case "lastName":
                    return "Ingresa un apellido valido porfavor.";
                case "email":
                    return "Ingresa un correo valido porfavor.";
                default:
                    return error.getDefaultMessage();
            }
        }
    }

    // Manejo de InvalidAppointmentTimeException
    @ExceptionHandler(InvalidAppointmentTimeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAppointmentTimeException(
            InvalidAppointmentTimeException exception,
            HttpServletRequest request
    ) {
        Map<String, Object> responseBody = this.buildResponse(
                "time",  // Nombre del campo
                exception.getMessage(),  // Mensaje de error
                "Por favor, seleccione una hora válida.",  // Descripción
                HttpStatus.BAD_REQUEST.value()  // Código de estado HTTP
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // manejo InvalidAppointmentDateException
    @ExceptionHandler(InvalidAppointmentDateException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAppointmentDateException(InvalidAppointmentDateException exception) {
        Map<String, Object> responseBody = this.buildResponse(
                "date",  // Nombre del campo
                exception.getMessage(),  // Mensaje de error
                "Por favor, seleccione una fecha válida.",  // Descripción
                HttpStatus.BAD_REQUEST.value()  // Código de estado HTTP
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // manejador de formato time y date
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleDateTimeParseException(
            DateTimeParseException ex,
            HttpServletRequest request
    ) {

        Map<String, Object> bodyResponse = new HashMap<>();
        bodyResponse.put("Error", ex.getMessage());
        bodyResponse.put("Description", "Ingresa un formato valido. Para fechas usar: 'yyyy-MM-dd' y para horas usar: 'HH:mm'");
        bodyResponse.put("Status", HttpStatus.BAD_REQUEST.value());
        bodyResponse.put("Url", request.getRequestURL());
        return ResponseEntity.badRequest().body(bodyResponse);
    }


    // Función de utilidad para construir el cuerpo de respuesta común

    private Map<String, Object> buildResponse(String field, String errorMessage, String description, int httpStatus) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", errorMessage);
        responseBody.put("field", field);
        responseBody.put("description", description);
        responseBody.put("status", httpStatus);
        return responseBody;
    }

    //excepcion generica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> genericHandlerException(
            HttpServletRequest request,
            Exception e
    ) {
        ApiError apiError = new ApiError();
//        apiError.setBackendMessage(e.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Error interno en el servidor");
        apiError.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    // ACCESS DENIED ESCEPTION
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> handlerAccessDeniedException(
//            HttpServletRequest request,
//            AccessDeniedException e
//    ) {
//        ApiError apiError = new ApiError();
//        apiError.setBackendMessage(e.getLocalizedMessage());
//        apiError.setUrl(request.getRequestURL().toString());
//        apiError.setMethod(request.getMethod());
//        apiError.setMessage("Acceso denegado: No tienes los permisos suficientes. Por favor, contacta al administrador si crees que es un error");
//        apiError.setTimestamp(LocalDateTime.now());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
//    }


}




