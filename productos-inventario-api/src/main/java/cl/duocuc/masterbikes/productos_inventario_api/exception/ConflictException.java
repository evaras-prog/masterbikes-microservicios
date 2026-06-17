package cl.duocuc.masterbikes.productos_inventario_api.exception;

public class ConflictException extends RuntimeException{
    public ConflictException(String mensaje){
        super(mensaje);
    }
}
