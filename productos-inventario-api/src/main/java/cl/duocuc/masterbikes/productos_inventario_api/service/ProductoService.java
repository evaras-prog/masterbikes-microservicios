package cl.duocuc.masterbikes.productos_inventario_api.service;

import cl.duocuc.masterbikes.productos_inventario_api.dto.ProductoRequest;
import cl.duocuc.masterbikes.productos_inventario_api.dto.ProductoResponse;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ConflictException;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ResourceNotFoundException;
import cl.duocuc.masterbikes.productos_inventario_api.model.CategoriaProducto;
import cl.duocuc.masterbikes.productos_inventario_api.model.Producto;
import cl.duocuc.masterbikes.productos_inventario_api.repository.CategoriaProductoRepository;
import cl.duocuc.masterbikes.productos_inventario_api.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.duocuc.masterbikes.productos_inventario_api.client.UsuarioClient;
import cl.duocuc.masterbikes.productos_inventario_api.dto.ApiResponse;
import cl.duocuc.masterbikes.productos_inventario_api.dto.UsuarioResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;
    private final CategoriaProductoRepository categoriaProductoRepository;
    private final UsuarioClient usuarioClient;

    public List<ProductoResponse> listarProductos(){

        log.info("Inicio de operacion: listar productos");

        List<Producto> productos = productoRepository.findAll();
        List<ProductoResponse> respuesta =  new ArrayList<>();

        for (Producto producto : productos){
            ProductoResponse dto = convertirAResponse(producto);
            respuesta.add(dto);
        }

        log.info("Proceso exitoso: se listaron {} productos", respuesta.size());

        return respuesta;
    }

    public ProductoResponse obtenerProductoPorId(Long idProducto){

        log.info("Inicio de operación: buscar producto por ID {}", idProducto);

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(()->{
                    log.warn("Búsqueda fallida: no se encontró producto con ID {}", idProducto);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        log.info("Proceso exitoso: producto encontrado con ID {}", idProducto);

        return convertirAResponse(producto);
    }

    public ProductoResponse registrarProducto(ProductoRequest request){

        log.info("Inicio de operación: registrar producto con código {}", request.getCodigo());

        if(productoRepository.existsByCodigo(request.getCodigo())){
            log.warn("Registro rechazado: ya existe un producto con código {}", request.getCodigo());
            throw new ConflictException("Ya existe un producto con ese código");
        }

        CategoriaProducto categoria = categoriaProductoRepository.findById(request.getIdCategoria())
                .orElseThrow(()->{
                    log.warn("Registro rechazado: categoria no encontrada con ID {}", request.getIdCategoria());
                    return new ResourceNotFoundException("Categoría de producto no encontrada");
                });

        //Regla de arriendo
        if ("S".equals(request.getDisponibleArriendo())){
            if (request.getPrecioArriendoDia() == null || request.getPrecioArriendoDia() <= 0){
                log.warn("Registro rechazado: producto disponible para arriendo sin precio de arriendo");
                throw new ConflictException("Un producto disponible para arriendo debe tener precio de arriendo mayor a 0");
            }
            if (request.getDepositoGarantia() == null || request.getDepositoGarantia() <= 0){
                log.warn("Registro rechazado: producto disponible para arriendo sin depósito de garantía");
                throw new ConflictException("Un producto disponible para arriendo debe tener depósito de garantía mayor a 0");
            }
        }

        //Regla de stock y disponibilidad
        if (request.getStock() == 0){
            if ("S". equals(request.getDisponibleVenta()) || "S".equals(request.getDisponibleArriendo())){
                log.warn("Registro rechazado: producto con stock 0 marcado como disponible");
                throw new ConflictException("Un producto con stock 0 no puede estar disponible para venta o arriendo");
            }
        }

        //Regla de inactivación
        if ("N".equals(request.getActivo())){
            request.setDisponibleVenta("N");
            request.setDisponibleArriendo("N");
            log.warn("Producto registrado como inactivo: disponibleVenta y disponibleArriendo forzados a N");
        }

        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setMarca(request.getMarca());
        producto.setModelo(request.getModelo());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setPrecioArriendoDia(request.getPrecioArriendoDia());
        producto.setDepositoGarantia(request.getDepositoGarantia());
        producto.setDisponibleVenta(request.getDisponibleVenta());
        producto.setDisponibleArriendo(request.getDisponibleArriendo());
        producto.setActivo(request.getActivo() == null ? "S" : request.getActivo());
        producto.setStock(request.getStock());
        producto.setCategoriaProducto(categoria);

        Producto productoGuardado = productoRepository.save(producto);

        log.info("Proceso exitoso: producto registrado con ID {}", productoGuardado.getIdProducto());

        return convertirAResponse(productoGuardado);
    }

    public ProductoResponse actualizarProducto(Long idProducto, ProductoRequest request){

        log.info("Inicio de operación: actualizar producto con ID {}", idProducto);

        Producto productoexistente = productoRepository.findById(idProducto)
                .orElseThrow(()->{
                    log.warn("Actualización rechazada: no se encontró producto con ID {}", idProducto);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        if(!productoexistente.getCodigo().equals(request.getCodigo())
            && productoRepository.existsByCodigo(request.getCodigo())) {
            log.warn("Actualización rechazada: ya existe un producto con código {}", request.getCodigo());
            throw new ConflictException("Ya existe un producto con ese código");
        }
            CategoriaProducto categoria = categoriaProductoRepository.findById(request.getIdCategoria())
                    .orElseThrow(()-> {
                        log.warn("Actualización rechazada: categoria no encontrada con ID {}", request.getIdCategoria());
                        return new ResourceNotFoundException("Categoria de producto no encontrada");
                    });

        //Regla de arriendo
        if ("S".equals(request.getDisponibleArriendo())){
            if (request.getPrecioArriendoDia() == null || request.getPrecioArriendoDia() <= 0){
                log.warn("Registro rechazado: producto disponible para arriendo sin precio de arriendo");
                throw new ConflictException("Un producto disponible para arriendo debe tener precio de arriendo mayor a 0");
            }
            if (request.getDepositoGarantia() == null || request.getDepositoGarantia() <= 0){
                log.warn("Registro rechazado: producto disponible para arriendo sin depósito de garantía");
                throw new ConflictException("Un producto disponible para arriendo debe tener depósito de garantía mayor a 0");
            }
        }

        //Regla de stock y disponibilidad
        if (request.getStock() == 0){
            if ("S". equals(request.getDisponibleVenta()) || "S".equals(request.getDisponibleArriendo())){
                log.warn("Registro rechazado: producto con stock 0 marcado como disponible");
                throw new ConflictException("Un producto con stock 0 no puede estar disponible para venta o arriendo");
            }
        }

        //Regla de inactivación
        if ("N".equals(request.getActivo())){
            request.setDisponibleVenta("N");
            request.setDisponibleArriendo("N");
            log.warn("Producto registrado como inactivo: disponibleVenta y disponibleArriendo forzados a N");
        }

            productoexistente.setCodigo(request.getCodigo());
            productoexistente.setNombre(request.getNombre());
            productoexistente.setMarca(request.getMarca());
            productoexistente.setModelo(request.getModelo());
            productoexistente.setPrecioVenta(request.getPrecioVenta());
            productoexistente.setPrecioArriendoDia(request.getPrecioArriendoDia());
            productoexistente.setDepositoGarantia(request.getDepositoGarantia());
            productoexistente.setDisponibleVenta(request.getDisponibleVenta());
            productoexistente.setDisponibleArriendo(request.getDisponibleArriendo());
            productoexistente.setActivo(request.getActivo() == null ? "S" : request.getActivo());
            productoexistente.setStock(request.getStock());
            productoexistente.setCategoriaProducto(categoria);

            Producto productoActualizado = productoRepository.save(productoexistente);

            log.info("Proceso exitoso: producto actualizado con ID {}", productoActualizado.getIdProducto());

            return convertirAResponse(productoActualizado);
    }

    public void eliminarProducto(Long idProducto){

        log.info("Inicio de operación: eliminar producto con ID {}", idProducto);

        if(!productoRepository.existsById(idProducto)){
            log.warn("Eliminación rechazada: no existe producto con ID {}", idProducto);
            throw new ResourceNotFoundException("No se puede eliminar: producto no existe");
        }

        productoRepository.deleteById(idProducto);

        log.info("Proceso exitoso: producto eliminado con ID {}", idProducto);
    }


    private ProductoResponse convertirAResponse(Producto producto){

        ProductoResponse response =  new ProductoResponse();

        response.setIdProducto(producto.getIdProducto());
        response.setCodigo(producto.getCodigo());
        response.setNombre(producto.getNombre());
        response.setMarca(producto.getMarca());
        response.setModelo(producto.getModelo());
        response.setPrecioVenta(producto.getPrecioVenta());
        response.setPrecioArriendoDia(producto.getPrecioArriendoDia());
        response.setDepositoGarantia(producto.getDepositoGarantia());
        response.setDisponibleVenta(producto.getDisponibleVenta());
        response.setDisponibleArriendo(producto.getDisponibleArriendo());
        response.setActivo(producto.getActivo());
        response.setStock(producto.getStock());

        response.setIdCategoria(producto.getCategoriaProducto().getIdCategoria());
        response.setNombreCategoria(producto.getCategoriaProducto().getNombre());

        return response;
    }



    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuarioDesdeMicroservicio(Long idUsuario){

        log.info("Inicio de operación: consultar usuario desde usuarios-cliente-api con ID {}", idUsuario);

        ApiResponse<UsuarioResponse> respuesta = usuarioClient.obtenerUsuarioPorId(idUsuario);

        if (respuesta == null || respuesta.isError() || respuesta.getData() == null){
            log.warn("Consulta rechazada: no se pudo obtener usuario con ID {}", idUsuario);
            throw new RuntimeException("No se pudo obtener el usuario desde usuarios-clientes-api");
        }

        log.info("Proceso exitoso: usuario obtenido desde usuarios-clientes-api con ID {}", idUsuario);

        return respuesta.getData();

    }
}
