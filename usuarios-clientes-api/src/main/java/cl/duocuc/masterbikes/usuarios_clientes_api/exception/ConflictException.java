package cl.duocuc.masterbikes.usuarios_clientes_api.exception;

public class ConflictException extends RuntimeException{
    public ConflictException(String mensaje){
        super(mensaje);
    }
}
