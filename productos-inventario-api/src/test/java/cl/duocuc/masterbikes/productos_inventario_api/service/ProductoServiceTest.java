package cl.duocuc.masterbikes.productos_inventario_api.service;

import cl.duocuc.masterbikes.productos_inventario_api.client.UsuarioClient;
import cl.duocuc.masterbikes.productos_inventario_api.dto.ApiResponse;
import cl.duocuc.masterbikes.productos_inventario_api.dto.ProductoRequest;
import cl.duocuc.masterbikes.productos_inventario_api.dto.ProductoResponse;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ConflictException;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ResourceNotFoundException;
import cl.duocuc.masterbikes.productos_inventario_api.model.CategoriaProducto;
import cl.duocuc.masterbikes.productos_inventario_api.model.Producto;
import cl.duocuc.masterbikes.productos_inventario_api.repository.CategoriaProductoRepository;
import cl.duocuc.masterbikes.productos_inventario_api.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import cl.duocuc.masterbikes.productos_inventario_api.dto.ApiResponse;
import cl.duocuc.masterbikes.productos_inventario_api.dto.UsuarioResponse;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaProductoRepository categoriaProductoRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private ProductoService productoService;


    private CategoriaProducto categoria;
    private Producto producto;

    @BeforeEach
    void setUp(){
        categoria = new CategoriaProducto();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Bicicletas MTB");

        producto = new Producto();
        producto.setIdProducto(1L);
        producto.setCodigo("BIC-001");
        producto.setNombre("Trek Marlin 5");
        producto.setMarca("Trek");
        producto.setPrecioVenta(450000.0);
        producto.setDisponibleVenta("S");
        producto.setDisponibleArriendo("N");
        producto.setActivo("S");
        producto.setStock(10);
        producto.setCategoriaProducto(categoria);
    }

    @Test
    @DisplayName("listarProductos: retorna lista con los productos existentes")
    void listarProductos_retornaListaConProductos(){
        //GIVEN
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        //WHEN
        List<ProductoResponse> resultado = productoService.listarProductos();

        //THEN
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("BIC-001");
        assertThat(resultado.get(0).getMarca()).isEqualTo("Trek");
    }

    @Test
    @DisplayName("obtenerProductoPorId: retorna el producto cuando no existe")
    void obtenerProductoPorId_cuandoExiste_retornaProducto(){
        //GIVEN
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        //WHEN
        ProductoResponse resultado = productoService.obtenerProductoPorId(1L);

        //THEN
        assertThat(resultado.getIdProducto()).isEqualTo(1L);
        assertThat(resultado.getMarca()).isEqualTo("Trek");
    }

    @Test
    @DisplayName("obtenerProductoPorId: lanza excepción cuando no existe")
    void obtenerProductoPorId_cuandoNoExiste_lanzaException(){
        //GIVEN
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        //THEN
        assertThatThrownBy(()->productoService.obtenerProductoPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producto no encontrado");
    }

    @Test
    @DisplayName("registrarProducto: lanza excepción cuando el código ya existe")
    void registrarProducto_codigoDuplicado_lanzaConflictException(){
        //GIVEN
        ProductoRequest request = new ProductoRequest();
        request.setCodigo("BIC-001");
        request.setNombre("Trek Marlin 7");
        request.setPrecioVenta(750000.0);
        request.setDisponibleArriendo("N");
        request.setStock(5);
        request.setIdCategoria(1L);

        when(productoRepository.existsByCodigo("BIC-001")).thenReturn(true);

        //THEN
        assertThatThrownBy(()->productoService.registrarProducto(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("código");
    }

    @Test
    @DisplayName("registrarProducto: lanza excepción cuando stock=0 y disponibleVenta=S")
    void registrarProducto_stockCeroConDisponibleVenta_lanzaConflictException(){
        //GIVEN
        ProductoRequest request = new ProductoRequest();
        request.setCodigo("BIC-002");
        request.setNombre("Trek Marlin 7");
        request.setPrecioVenta(750000.0);
        request.setDisponibleVenta("S");
        request.setDisponibleArriendo("N");
        request.setStock(0);
        request.setIdCategoria(1L);

        when(productoRepository.existsByCodigo("BIC-002")).thenReturn(false);
        when(categoriaProductoRepository.findById(1L)).thenReturn(Optional.of(categoria));

        //THEN
        assertThatThrownBy(()->productoService.registrarProducto(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("stock 0");
    }

    @Test
    @DisplayName("actualizarProducto: actualiza correctamente cuando el producto existe")
    void actualizarProducto_exitoso(){
        //GIVEN
        ProductoRequest request = new ProductoRequest();
        request.setCodigo("BIC-001");
        request.setNombre("Trek Marlin 7");
        request.setMarca("Trek");
        request.setPrecioVenta(550000.0);
        request.setDisponibleVenta("S");
        request.setStock(5);
        request.setIdCategoria(1L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaProductoRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        //WHEN
        ProductoResponse resultado = productoService.actualizarProducto(1L, request);

        //THEN
        assertThat(resultado).isNotNull();
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("eliminarProducto: elimina correctamente cuando el producto existe")
    void eliminarProducto_exitoso(){
        //GIVEN
        when(productoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1L);

        //WHEN
        productoService.eliminarProducto(1L);

        //THEN
        verify(productoRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("obtenerUsuarioDesdeMicroservicio: retorna usuario cuando Feign responde correctamente")
    void obtenerUsuarioDesdeMicroservicio_exitoso(){
        //GIVEN
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setIdUsuario(1L);
        usuarioResponse.setRut("12345678K");

        ApiResponse<UsuarioResponse> apiResponse = new ApiResponse<>(200, "OK", false, usuarioResponse);

        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(apiResponse);

        //WHEN
        UsuarioResponse resultado = productoService.obtenerUsuarioDesdeMicroservicio(1L);

        //THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getRut()).isEqualTo("12345678K");
    }
}
