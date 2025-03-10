package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.domain.exception.AsignarEmpleadoRestaurantException;
import com.example.plazoleta.domain.exception.DishException;
import com.example.plazoleta.domain.exception.PedidoException;
import com.example.plazoleta.domain.exception.RestaurantException;
import com.example.plazoleta.infraestructure.constants.GlobalExceptionHandlerConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoException.class)
    public ResponseEntity<Map<String, Object>> handlePedidoException(PedidoException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(GlobalExceptionHandlerConstants.ERROR, true);
        response.put(GlobalExceptionHandlerConstants.MESSAGE, ex.getMessage());
        response.put(GlobalExceptionHandlerConstants.TIMESTAMP, LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(GlobalExceptionHandlerConstants.ERROR, true);
        response.put(GlobalExceptionHandlerConstants.MESSAGE, GlobalExceptionHandlerConstants
                .UNEXPECTED_ERROR + ex.getMessage());
        response.put(GlobalExceptionHandlerConstants.TIMESTAMP, LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AsignarEmpleadoRestaurantException.class)
    public ResponseEntity<Map<String, Object>> handleAsignarEmpleadoRestaurantException(
            AsignarEmpleadoRestaurantException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(GlobalExceptionHandlerConstants.ERROR, true);
        response.put(GlobalExceptionHandlerConstants.MESSAGE, ex.getMessage());
        response.put(GlobalExceptionHandlerConstants.TIMESTAMP, LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DishException.class)
    public ResponseEntity<Map<String, Object>> handleDishException(
            DishException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(GlobalExceptionHandlerConstants.ERROR, true);
        response.put(GlobalExceptionHandlerConstants.MESSAGE, ex.getMessage());
        response.put(GlobalExceptionHandlerConstants.TIMESTAMP, LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestaurantException.class)
    public ResponseEntity<Map<String, Object>> handleRestaurantException(
            RestaurantException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(GlobalExceptionHandlerConstants.ERROR, true);
        response.put(GlobalExceptionHandlerConstants.MESSAGE, ex.getMessage());
        response.put(GlobalExceptionHandlerConstants.TIMESTAMP, LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
