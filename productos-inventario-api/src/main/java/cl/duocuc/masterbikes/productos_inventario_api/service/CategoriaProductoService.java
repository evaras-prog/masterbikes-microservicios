package cl.duocuc.masterbikes.productos_inventario_api.service;

import cl.duocuc.masterbikes.productos_inventario_api.dto.CategoriaProductoRequest;
import cl.duocuc.masterbikes.productos_inventario_api.dto.CategoriaProductoResponse;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ConflictException;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ResourceNotFoundException;
import cl.duocuc.masterbikes.productos_inventario_api.model.CategoriaProducto;
import cl.duocuc.masterbikes.productos_inventario_api.repository.CategoriaProductoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaProductoService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaProductoService.class);

    private final CategoriaProductoRepository categoriaProductoRepository;

    @Transactional(readOnly = true)
    public List<CategoriaProductoResponse> listarCategorias(){

        log.info("Inicio de operación: listar categorías de productos");

        List<CategoriaProducto> categorias = categoriaProductoRepository.findAll();
        List<CategoriaProductoResponse> respuesta = new ArrayList<>();

        for (CategoriaProducto categoria : categorias){
            CategoriaProductoResponse dto = convertirAResponse(categoria);
            respuesta.add(dto);
        }

        log.info("Proceso exitoso: se listaron {} categorias", respuesta.size());

        return respuesta;
    }


    @Transactional(readOnly = true)
    public CategoriaProductoResponse obtenerCategoriaPorId(Long idCategoria){

        log.info("Inicio de operación: buscar categoría por ID {}", idCategoria);

        CategoriaProducto categoria = categoriaProductoRepository.findById(idCategoria)
                .orElseThrow(()-> {
                    log.warn("Búsqueda fallida: no se encontró categoría con ID {}", idCategoria);
                    return new ResourceNotFoundException("Categoría de producto no encontrada");
                });

        log.info("Proceso exitoso: categoría encontrada con ID {}", idCategoria);

        return convertirAResponse(categoria);
    }


    public CategoriaProductoResponse registrarCategoria(CategoriaProductoRequest request){

        log.info("Inicio de operación: registar categoría con nombre {}", request.getNombre());

        if(categoriaProductoRepository.existsByNombre(request.getNombre())){
            log.warn("Registro rechazado: ya existe una categoria con nombre {}",request.getNombre());
            throw new ConflictException("Ya existe una categoría con ese nombre");
        }

        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        CategoriaProducto categoriaGuardada = categoriaProductoRepository.save(categoria);

        log.info("Proceso exitoso: categoria registrada con ID {}", categoriaGuardada.getIdCategoria());

        return convertirAResponse(categoriaGuardada);
    }



    public CategoriaProductoResponse actualizarCategoria(Long idCategoria, CategoriaProductoRequest request){

        log.info("Inicio de operación: actualizar categoria con ID {}", idCategoria);

        CategoriaProducto categoriaExistente = categoriaProductoRepository.findById(idCategoria)
                .orElseThrow(()->{
                    log.warn("Actualización rechazada: no se encontró categoría con ID {}",idCategoria);
                    return new ResourceNotFoundException("Categoria de producto no encontrada");
                });

        if (!categoriaExistente.getNombre().equals(request.getNombre())
                && categoriaProductoRepository.existsByNombre(request.getNombre())){
            log.warn("Actualización rechazada: ya existe una categoría con nombre {}", request.getNombre());
            throw new ConflictException("Ya existe una categoría con ese nombre");
        }

        categoriaExistente.setNombre(request.getNombre());
        categoriaExistente.setDescripcion(request.getDescripcion());

        CategoriaProducto categoriaActualizada = categoriaProductoRepository.save(categoriaExistente);

        log.info("Proceso exitoso: categoria actualizada con ID {}", categoriaActualizada.getIdCategoria());

        return convertirAResponse(categoriaActualizada);
    }


    public void eliminarCategoria(Long idCategoria){

        log.info("Inicio de operación: eliminar categoria con ID {}", idCategoria);

        if(!categoriaProductoRepository.existsById(idCategoria)){
            log.warn("Eliminación rechazada: no existe categoría con ID {}", idCategoria);
            throw new ResourceNotFoundException("No se puede eliminar: categoría no existe");
        }
        categoriaProductoRepository.deleteById(idCategoria);

        log.info("Proceso existoso: categoria eliminada con ID {}", idCategoria);
    }


    private CategoriaProductoResponse convertirAResponse(CategoriaProducto categoria){

        CategoriaProductoResponse response = new CategoriaProductoResponse();

        response.setIdCategoria(categoria.getIdCategoria());
        response.setNombre(categoria.getNombre());
        response.setDescripcion(categoria.getDescripcion());

        return response;
    }
}
