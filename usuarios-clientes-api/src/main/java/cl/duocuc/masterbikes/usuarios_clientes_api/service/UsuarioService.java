package cl.duocuc.masterbikes.usuarios_clientes_api.service;

import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ApiResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.exception.ConflictException;
import cl.duocuc.masterbikes.usuarios_clientes_api.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioRequest;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.model.TipoUsuario;
import cl.duocuc.masterbikes.usuarios_clientes_api.model.Usuario;
import cl.duocuc.masterbikes.usuarios_clientes_api.repository.TipoUsuarioRepository;
import cl.duocuc.masterbikes.usuarios_clientes_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duocuc.masterbikes.usuarios_clientes_api.client.ProductoClient;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ProductoResponse;
import feign.FeignException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final ProductoClient productoClient;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuarios(){

        log.info("Inicio de operación: listar usuarios");

        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponse> respuesta = new ArrayList<>();

        for(Usuario usuario : usuarios){
            UsuarioResponse dto = convertirAResponse(usuario);
            respuesta.add(dto);
        }

        log.info("Proceso exitoso: se listaron {} usuarios", respuesta.size());

        return respuesta;
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuarioPorId(Long id){

        log.info("Inicio de operación: buscar usuario por ID {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(()->{
                    log.warn("Búsqueda fallida: no se encontró usuario con ID {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado");
                });

        log.info("Proceso exitoso: usuario encontrado con ID {}", id);

        return convertirAResponse(usuario);
    }

    public UsuarioResponse registrarUsuario(UsuarioRequest request){

        log.info("Inicio de operación: registrar usuario con RUT {}", request.getRut());

        if (usuarioRepository.existsByRut(request.getRut())){
            log.warn("Registro rechazado: ya existe un usuario con RUT {}", request.getRut());
            throw new ConflictException("Ya existe un usuario con ese RUT");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())){
            log.warn("Registro rechazado: ya existe");
            throw new ConflictException("Ya existe un usuario con ese correo");
        }

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(request.getIdTipoUsuario())
                .orElseThrow(()->{
                    log.warn("Registro rechazado: tipo de usuario no encontrado con ID {}", request.getIdTipoUsuario());
                    return new ResourceNotFoundException("Tipo de usuario no encontrado");
                });

        Usuario usuario = new Usuario();
        usuario.setRut(request.getRut());
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(request.getPassword());
        usuario.setTelefono(request.getTelefono());
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setActivo(request.getActivo() == null ? "S" : request.getActivo());
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setIdSucursal(request.getIdSucursal());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        log.info("Proceso exitoso: usuario registrado con ID {}", usuarioGuardado.getIdUsuario());

        return convertirAResponse(usuarioGuardado);
    }

    public UsuarioResponse actualizarUsuario(Long idUsuario, UsuarioRequest request){

        log.info("Inicio de operación: actualizar usuario con ID {}", idUsuario);

        Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
                .orElseThrow(()->{
                    log.warn("Actualización rechazada: no se encontró usuario con ID {}", idUsuario);
                    return new ResourceNotFoundException("Usuario no encontrado");
                });

        if (!usuarioExistente.getRut().equals(request.getRut())
                && usuarioRepository.existsByRut(request.getRut())){
            log.warn("Actualización rechazada: ya existe un usurio con RUT {}", request.getRut());
            throw new ConflictException("Ya existe un usuario con ese RUT");
        }

        if (!usuarioExistente.getCorreo().equals(request.getCorreo())
                && usuarioRepository.existsByCorreo(request.getCorreo())){
            log.warn("Actualización rechazada: ya existe un usuario con correo {}", request.getCorreo());
            throw new ConflictException("Ya existe un usuario con ese correo");
        }

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(request.getIdTipoUsuario())
                .orElseThrow(()-> {
                    log.warn("Actualización rechazada: tipo de usuario no encontrado con ID {}", request.getIdTipoUsuario());
                    return new ResourceNotFoundException("Tipo de usuario no encontrado");
                });

        usuarioExistente.setRut(request.getRut());
        usuarioExistente.setNombres(request.getNombres());
        usuarioExistente.setApellidos(request.getApellidos());
        usuarioExistente.setCorreo(request.getCorreo());
        usuarioExistente.setPassword(request.getPassword());
        usuarioExistente.setTelefono(request.getTelefono());
        usuarioExistente.setActivo(request.getActivo() == null ? "S" : request.getActivo());
        usuarioExistente.setTipoUsuario(tipoUsuario);
        usuarioExistente.setIdSucursal(request.getIdSucursal());

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);

        log.info("Proceso exitoso: usuario actualizado con ID {}", usuarioActualizado.getIdUsuario());

        return convertirAResponse(usuarioActualizado);
    }

    public void eliminarUsuario(Long id){

        log.info("Inicio de operación: eliminar usuario con ID {}", id);

        if (!usuarioRepository.existsById(id)){
            log.warn("Eliminación rechazada: no existe usuario con ID {}", id );
            throw new ResourceNotFoundException("No se puede eliminar: usuario no existe");
        }
        usuarioRepository.deleteById(id);

        log.info("Proceso exitoso: usuario eliminado con ID {}", id);

    }


    @Transactional(readOnly = true)
    public ProductoResponse obtenerProductoDesdeMicroservicio(Long idProducto){

        log.info("Inicio de operación: consultar producto desde productos-inventario-api con ID {}", idProducto);

        ApiResponse<ProductoResponse> respuesta = productoClient.obtenerProductoPorId(idProducto);

        if (respuesta == null || respuesta.isError() || respuesta.getData() == null){
            log.warn("Consulta rechazada: no se pudo obtener producto con ID {}", idProducto);
            throw new RuntimeException("No se pudo obtener productos desde productos-inventario-api");
        }

        log.info("Proceso exitoso: producto obtenido desde productos-inventario-api con ID {}", idProducto);

        return respuesta.getData();
    }


    private UsuarioResponse convertirAResponse(Usuario usuario){

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getRut(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getFechaRegistro(),
                usuario.getActivo(),
                usuario.getTipoUsuario().getIdTipoUsuario(),
                usuario.getTipoUsuario().getNombre(),
                usuario.getIdSucursal()
        );
    }
}



