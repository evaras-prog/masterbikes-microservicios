package cl.duocuc.masterbikes.usuarios_clientes_api.service;

import cl.duocuc.masterbikes.usuarios_clientes_api.client.ProductoClient;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ApiResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ProductoResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioRequest;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.exception.ConflictException;
import cl.duocuc.masterbikes.usuarios_clientes_api.exception.ResourceNotFoundException;
import cl.duocuc.masterbikes.usuarios_clientes_api.model.TipoUsuario;
import cl.duocuc.masterbikes.usuarios_clientes_api.model.Usuario;
import cl.duocuc.masterbikes.usuarios_clientes_api.repository.TipoUsuarioRepository;
import cl.duocuc.masterbikes.usuarios_clientes_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private TipoUsuario tipoUsuario;
    private UsuarioRequest request;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setIdTipoUsuario(1L);
        tipoUsuario.setNombre("CLIENTE");
        tipoUsuario.setDescripcion("Cliente regular");

        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setRut("11111111-1");
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan@correo.com");
        usuario.setPassword("123456");
        usuario.setTelefono("987654321");
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setActivo("S");
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setIdSucursal(1L);

        request = new UsuarioRequest();
        request.setRut("11111111-1");
        request.setNombres("Juan");
        request.setApellidos("Pérez");
        request.setCorreo("juan@correo.com");
        request.setPassword("123456");
        request.setTelefono("987654321");
        request.setActivo("S");
        request.setIdTipoUsuario(1L);
        request.setIdSucursal(1L);
    }

    // ---------- listarUsuarios ----------

    @Test
    void listarUsuarios_debeRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponse> resultado = usuarioService.listarUsuarios();

        assertEquals(1, resultado.size());
        assertEquals(usuario.getCorreo(), resultado.get(0).getCorreo());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void listarUsuarios_debeRetornarListaVacia_siNoHayUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<UsuarioResponse> resultado = usuarioService.listarUsuarios();

        assertTrue(resultado.isEmpty());
    }

    // ---------- obtenerUsuarioPorId ----------

    @Test
    void obtenerUsuarioPorId_debeRetornarUsuario_siExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponse resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertNotNull(resultado);
        assertEquals(usuario.getRut(), resultado.getRut());
    }

    @Test
    void obtenerUsuarioPorId_debeLanzarExcepcion_siNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.obtenerUsuarioPorId(99L));
    }

    // ---------- registrarUsuario ----------

    @Test
    void registrarUsuario_debeGuardarUsuario_cuandoDatosSonValidos() {
        when(usuarioRepository.existsByRut(request.getRut())).thenReturn(false);
        when(usuarioRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponse resultado = usuarioService.registrarUsuario(request);

        assertNotNull(resultado);
        assertEquals(request.getCorreo(), resultado.getCorreo());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_debeLanzarConflicto_siRutYaExiste() {
        when(usuarioRepository.existsByRut(request.getRut())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> usuarioService.registrarUsuario(request));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarUsuario_debeLanzarConflicto_siCorreoYaExiste() {
        when(usuarioRepository.existsByRut(request.getRut())).thenReturn(false);
        when(usuarioRepository.existsByCorreo(request.getCorreo())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> usuarioService.registrarUsuario(request));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarUsuario_debeLanzarNotFound_siTipoUsuarioNoExiste() {
        when(usuarioRepository.existsByRut(request.getRut())).thenReturn(false);
        when(usuarioRepository.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(tipoUsuarioRepository.findById(request.getIdTipoUsuario())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.registrarUsuario(request));

        verify(usuarioRepository, never()).save(any());
    }

    // ---------- actualizarUsuario ----------

    @Test
    void actualizarUsuario_debeActualizarDatos_cuandoUsuarioExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByRut(any())).thenReturn(false);
        when(usuarioRepository.existsByCorreo(any())).thenReturn(false);
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponse resultado = usuarioService.actualizarUsuario(1L, request);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void actualizarUsuario_debeLanzarNotFound_siUsuarioNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.actualizarUsuario(99L, request));

        verify(usuarioRepository, never()).save(any());
    }

    // ---------- eliminarUsuario ----------

    @Test
    void eliminarUsuario_debeEliminar_siUsuarioExiste() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarUsuario_debeLanzarNotFound_siUsuarioNoExiste() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.eliminarUsuario(99L));

        verify(usuarioRepository, never()).deleteById(any());
    }

    // ---------- obtenerProductoDesdeMicroservicio ----------

    @Test
    void obtenerProductoDesdeMicroservicio_debeRetornarProducto_siRespuestaEsValida() {
        ProductoResponse productoResponse = mock(ProductoResponse.class);
        ApiResponse<ProductoResponse> apiResponse = new ApiResponse<>(200, "OK", false, productoResponse);

        when(productoClient.obtenerProductoPorId(1L)).thenReturn(apiResponse);

        ProductoResponse resultado = usuarioService.obtenerProductoDesdeMicroservicio(1L);

        assertEquals(productoResponse, resultado);
    }

    @Test
    void obtenerProductoDesdeMicroservicio_debeLanzarExcepcion_siRespuestaEsError() {
        ApiResponse<ProductoResponse> apiResponse = new ApiResponse<>(404, "No encontrado", true, null);

        when(productoClient.obtenerProductoPorId(99L)).thenReturn(apiResponse);

        assertThrows(RuntimeException.class,
                () -> usuarioService.obtenerProductoDesdeMicroservicio(99L));
    }

    @Test
    void obtenerProductoDesdeMicroservicio_debeLanzarExcepcion_siRespuestaEsNull() {
        when(productoClient.obtenerProductoPorId(99L)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> usuarioService.obtenerProductoDesdeMicroservicio(99L));
    }
}


