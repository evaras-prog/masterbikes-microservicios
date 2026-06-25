package cl.duocuc.masterbikes.usuarios_clientes_api.service;

import cl.duocuc.masterbikes.usuarios_clientes_api.client.ProductoClient;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ApiResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.ProductoResponse;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioRequest;
import cl.duocuc.masterbikes.usuarios_clientes_api.exception.ConflictException;
import cl.duocuc.masterbikes.usuarios_clientes_api.exception.ResourceNotFoundException;
import cl.duocuc.masterbikes.usuarios_clientes_api.repository.TipoUsuarioRepository;
import cl.duocuc.masterbikes.usuarios_clientes_api.repository.UsuarioRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duocuc.masterbikes.usuarios_clientes_api.model.TipoUsuario;
import cl.duocuc.masterbikes.usuarios_clientes_api.model.Usuario;
import cl.duocuc.masterbikes.usuarios_clientes_api.dto.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private UsuarioService usuarioService;

    private TipoUsuario tipoUsuario;
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        tipoUsuario = new TipoUsuario(1L, "CLIENTE", "Cliente final");

        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setRut("12345678K");
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");
        usuario.setCorreo("juan@test.cl");
        usuario.setPassword("clave123");
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setActivo("S");
        usuario.setTipoUsuario(tipoUsuario);
    }

    @Test
    @DisplayName("listarUsuarios: retorna lista con usuarios existentes")
    void listarUsuarios_retornaListaConUsuarios(){
        //GIVEN
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        //WHEN
        List<UsuarioResponse> resultado = usuarioService.listarUsuarios();

        //THEN
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRut()).isEqualTo("12345678K");
    }

    @Test
    @DisplayName("obtenerUsuarioPorId: retorna el usuario cuando existe")
    void obtenerUsuarioPorId_cuandoExiste_retornaUsuario(){
        //GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        //WHEN
        UsuarioResponse resultado = usuarioService.obtenerUsuarioPorId(1L);

        //THEN
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getCorreo()).isEqualTo("juan@test.cl");
    }

    @Test
    @DisplayName("obtenerUsuarioPorId: lanza excepción cuando no existe")
    void obtnerUsuarioPorId_cuandoNoExiste_lanzaException(){
        //GIVEN: el repositorio no encuentra nada
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        //THEN: verificamos que se lanza la excepción correcta
        assertThatThrownBy(()->usuarioService.obtenerUsuarioPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    @DisplayName("registrarUsuario: lanza excepción cuando el rut ya existe")
    void registrarUsuario_rutDuplicado_lanzaConflictException(){
        //GIVEN
        UsuarioRequest request = new UsuarioRequest();
        request.setRut("12345678K");
        request.setNombres("Pedro");
        request.setApellidos("González");
        request.setCorreo("pedro@test.cl");
        request.setPassword("clave123");
        request.setIdTipoUsuario(1L);

        when(usuarioRepository.existsByRut("12345678K")).thenReturn(true);

        //THEN
        assertThatThrownBy(()->usuarioService.registrarUsuario(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("RUT");
    }

    @Test
    @DisplayName("eliminarUsuario: elimina correctamente cuando el usuario existe")
    void eliminarUsuario_exitoso(){
        //GIVEN
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        //WHEN
        usuarioService.eliminarUsuario(1L);

        //THEN: verificamos que deleteById fue llamado exactamente una vez
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("actualizarUsuario: actualiza correctamente cuando el usuario existe")
    void actualizarUsuario_exitoso(){
        //GIVEN
        UsuarioRequest request = new UsuarioRequest();
        request.setRut("12345678K");
        request.setNombres("Juan Modificado");
        request.setApellidos("Pérez");
        request.setCorreo("juan@test.cl");
        request.setPassword("nueva123");
        request.setIdTipoUsuario(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        //WHEN
        UsuarioResponse resultado = usuarioService.actualizarUsuario(1L, request);

        //THEN
        assertThat(resultado).isNotNull();
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }


    @Test
    @DisplayName("obtenerProductoDesdeMicroservicio: retorna producto cuando Feign responde correctamente")
    void obtenerProductoDesdeMicroservicio_exitoso(){
        //GIVEN
        ProductoResponse productoResponse = new ProductoResponse();
        productoResponse.setIdProducto(1L);
        productoResponse.setCodigo("BIC-001");

        ApiResponse<ProductoResponse> apiResponse = new ApiResponse<>(200, "OK", false, productoResponse);

        when(productoClient.obtenerProductoPorId(1L)).thenReturn(apiResponse);

        //WHEN
        ProductoResponse resultado = usuarioService.obtenerProductoDesdeMicroservicio(1L);

        //THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCodigo()).isEqualTo("BIC-001");
    }
}
