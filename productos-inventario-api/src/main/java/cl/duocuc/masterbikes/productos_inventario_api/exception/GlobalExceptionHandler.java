package cl.duocuc.masterbikes.productos_inventario_api.exception;

import cl.duocuc.masterbikes.productos_inventario_api.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> manejarValidaciones(MethodArgumentNotValidException ex){

        String mensajeError = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        log.error("Error de validación: {}", mensajeError);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        mensajeError,
                        true,
                        null
                ));
    }


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> manejarConflicto(ConflictException ex) {
        log.error("Conflicto de datos: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        true,
                        null
                ));
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> manejarNotFound(ResourceNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        true,
                        null
                ));
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> manejarRutimeException(RuntimeException ex){

        log.error("Error de lógica de negocio: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        true,
                        null
                ));
    }



    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse<Object>> manejarFeignException(FeignException ex){

        log.error("Error de comunicación con otro microservicio: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_GATEWAY.value(),
                        "No se pudo obtener la información desde usuarios-clientes-api",
                        true,
                        null
                ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> manejarException(Exception ex){

        log.error("Error interno no controlado:{}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error interno del servidor",
                        true,
                        null
                ));
    }
}
