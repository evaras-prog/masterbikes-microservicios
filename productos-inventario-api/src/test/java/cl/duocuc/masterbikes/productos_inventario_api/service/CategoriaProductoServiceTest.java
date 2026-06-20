package cl.duocuc.masterbikes.productos_inventario_api.service;

import cl.duocuc.masterbikes.productos_inventario_api.dto.CategoriaProductoRequest;
import cl.duocuc.masterbikes.productos_inventario_api.dto.CategoriaProductoResponse;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ConflictException;
import cl.duocuc.masterbikes.productos_inventario_api.exception.ResourceNotFoundException;
import cl.duocuc.masterbikes.productos_inventario_api.model.CategoriaProducto;
import cl.duocuc.masterbikes.productos_inventario_api.repository.CategoriaProductoRepository;
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

@ExtendWith(MockitoExtension.class)
public class CategoriaProductoServiceTest {

    @Mock
    private CategoriaProductoRepository categoriaProductoRepository;

    @InjectMocks
    private CategoriaProductoService categoriaProductoService;

    private CategoriaProducto categoria;

    @BeforeEach
    void setUp(){
        categoria = new CategoriaProducto();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Bicicletas MTB");
        categoria.setDescripcion("Bicicletas de montaña");
    }


    @Test
    @DisplayName("listarCategorias: retorna lista con las categorias existentes")
    void listarCategorias_retornaListaConCategorias(){
        //GIVEN
        when(categoriaProductoRepository.findAll()).thenReturn(List.of(categoria));

        //WHEN
        List<CategoriaProductoResponse> resultado = categoriaProductoService.listarCategorias();

        //THEN
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Bicicletas MTB");
    }

    @Test
    @DisplayName("obtenerCategoriaPorId: retorna la categoría cuando existe")
    void obtenerCategoriaPorId_cuandoExiste_retornaCategoria(){
        //GIVEN
        when(categoriaProductoRepository.findById(1L)).thenReturn(Optional.of(categoria));

        //WHEN
        CategoriaProductoResponse resultado = categoriaProductoService.obtenerCategoriaPorId(1L);

        //THEN
        assertThat(resultado.getIdCategoria()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Bicicletas MTB");
    }


    @Test
    @DisplayName("obtenerCategoriaPorId: lanza excepción cuando no existe")
    void obtenerCategoriaPorId_cuandoNoExiste_lanzaException(){
        //GIVEN
        when(categoriaProductoRepository.findById(99L)).thenReturn(Optional.empty());

        //THEN
        assertThatThrownBy(()->categoriaProductoService.obtenerCategoriaPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoría de producto no encontrada");
    }


    @Test
    @DisplayName("registrarCategoria: lanza excepción cuando el nombre ya existe")
    void registrarCategoria_nombreDuplicado_lanzaConflictException(){
        //GIVEN
        CategoriaProductoRequest request = new CategoriaProductoRequest();
        request.setNombre("Bicicletas MTB");
        request.setDescripcion("Bicicletas de montaña");

        when(categoriaProductoRepository.existsByNombre("Bicicletas MTB")).thenReturn(true);

        //THEN
        assertThatThrownBy(()->categoriaProductoService.registrarCategoria(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Ya existe una categoría con ese nombre");
    }

    @Test
    @DisplayName("actualizarCategoria: actualiza correctamente cuando la categoría existe")
    void actualizarCategoria_exitoso(){
        //GIVEN
        CategoriaProductoRequest request = new CategoriaProductoRequest();
        request.setNombre("Bicicletas MTB Pro");
        request.setDescripcion("Bicicletas de montaña profesionales");

        when(categoriaProductoRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaProductoRepository.save(any(CategoriaProducto.class))).thenReturn(categoria);

        //WHEN
        CategoriaProductoResponse resultado = categoriaProductoService.actualizarCategoria(1L, request);

        //THEN
        assertThat(resultado).isNotNull();
        verify(categoriaProductoRepository, times(1)).save(any(CategoriaProducto.class));
    }


    @Test
    @DisplayName("eliminarCategoria: elimina correctamente cuando la categoría existe")
    void eliminarCategoria_exitoso(){
        //GIVEN
        when(categoriaProductoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoriaProductoRepository).deleteById(1L);

        //WHEN
        categoriaProductoService.eliminarCategoria(1L);

        //THEN
        verify(categoriaProductoRepository, times(1)).deleteById(1L);
    }


}
