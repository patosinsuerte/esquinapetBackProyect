package com.esquinaPet.veterinariabackend.infra.errors;


import com.esquinaPet.veterinariabackend.dto.ApiError;
import com.esquinaPet.veterinariabackend.infra.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePhoneAlreadyExistsException(UserAlreadyExistsException exception) {
        String errorMessage = exception.getMessage();
        String fieldName = exception.getFieldName(); // Obtener el nombre del campo afectado

        // Determinar la acción y el mensaje de error según el tipo de excepción
        String description = ""; // Valor predeterminado
        if (fieldName.equals("email")) {
            description = "The email is already in use, please try another";
        }
        if (fieldName.equals("phone")) {
            description = "The phone is already in use, please try another";
        }
        if (fieldName.equals("rut")) {
            description = "The rut is already in use, please try another";
        }

        // Construir el cuerpo de la respuesta
        Map<String, Object> responseBody = buildResponse(
                fieldName,
                errorMessage,
                description,
                HttpStatus.BAD_REQUEST.value()
        );

        // Devolver la respuesta
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }




    // email NO EXISTE

    @ExceptionHandler(EmailHasNotExistException.class)
    public ResponseEntity<Map<String, Object>> handleEmailHasNotExistException(EmailHasNotExistException ex) {
        Map<String, Object> responseBody = this.buildResponse(
                "email",  // Nombre del campo
                ex.getMessage(),  // Mensaje de error
                "Por favor, Ingrese un correo valido",  // Descripción
                HttpStatus.BAD_REQUEST.value()  // Código de estado HTTP
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }


    // wrong password
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleWrongPasswordException(WrongPasswordException ex) {
        Map<String, Object> responseBody = this.buildResponse(
                "password",  // Nombre del campo
                ex.getMessage(),  // Mensaje de error
                "Contrasena invalida, por favor intente nuevamente",  // Descripción
                HttpStatus.BAD_REQUEST.value()  // Código de estado HTTP
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }



    // User info NOT match exception handler

    @ExceptionHandler(UserInfoNotMatchException.class)
    public ResponseEntity<Map<String, Object>> handleUserInfoNotMatchException(UserInfoNotMatchException e){

        Map<String, Object> responseBody = this.buildResponse(
                "Campo con error: " + e.getField(),
                 "Por favor ingresa los valores adecuados",
                "Los campos: " + e.getValueInRequest() + " " + e.getValueInProfile() + " no coinciden",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }



    @ExceptionHandler(TimeIsNotAvailable.class)
    public ResponseEntity<Map<String, Object>> handlerTimeIsNotAvailable(TimeIsNotAvailable e){

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("Filed", e.getField());
        responseBody.put("Message", e.getMessage());
        responseBody.put("Code error", e.getCode());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }








}




