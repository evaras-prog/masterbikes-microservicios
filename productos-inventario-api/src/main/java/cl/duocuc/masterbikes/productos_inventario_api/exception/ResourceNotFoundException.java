package cl.duocuc.masterbikes.productos_inventario_api.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje){
        super(mensaje);
    }
}
