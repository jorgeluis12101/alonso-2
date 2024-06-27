// CategoriaController.java
package com.example.demo.webController;

import com.example.demo.domain.dto.CategoriaConEventosDTO;
import com.example.demo.domain.dto.CategoriaDTO;
import com.example.demo.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping("/crear")
    public ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        return new ResponseEntity<>(categoriaService.crearCategoria(categoriaDTO), HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        List<CategoriaDTO> categorias = categoriaService.obtenerCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/con-eventos")
    public ResponseEntity<List<CategoriaConEventosDTO>> listarCategoriasConEventos() {
        List<CategoriaConEventosDTO> categorias = categoriaService.obtenerCategoriasConEventos();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        return new ResponseEntity<>(categoriaService.obtenerCategoriaPorId(id), HttpStatus.OK);
    }

    @GetMapping("/obtener-con-eventos/{id}")
    public ResponseEntity<CategoriaConEventosDTO> obtenerCategoriaConEventosPorId(@PathVariable Long id) {
        return new ResponseEntity<>(categoriaService.obtenerCategoriaConEventosPorId(id), HttpStatus.OK);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        return new ResponseEntity<>(categoriaService.actualizarCategoria(id, categoriaDTO), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
