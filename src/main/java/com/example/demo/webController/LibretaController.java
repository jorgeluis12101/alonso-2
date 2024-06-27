package com.example.demo.webController;

import com.example.demo.domain.dto.LibretaDTO;
import com.example.demo.domain.entity.Libreta;
import com.example.demo.services.LibretaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libretas")
@RequiredArgsConstructor
public class LibretaController {

    private final LibretaService libretaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearLibreta(@RequestBody LibretaDTO libretaDTO) {
        try {
            LibretaDTO nuevaLibreta = libretaService.registrarLibreta(libretaDTO.getNombre());
            return ResponseEntity.ok(nuevaLibreta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibretaDTO> obtenerLibreta(@PathVariable Long id) {
        LibretaDTO libreta = libretaService.obtenerLibretaPorId(id);
        if (libreta != null) {
            return ResponseEntity.ok(libreta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<LibretaDTO>> obtenerLibretasPorUsuario() {
        List<LibretaDTO> libretas = libretaService.obtenerLibretasPorUsuario();
        return ResponseEntity.ok(libretas);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarLibreta(@PathVariable Long id) {
        libretaService.eliminarLibreta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LibretaDTO>> buscarLibretas(@RequestParam String nombre) {
        List<LibretaDTO> libretas = libretaService.buscarLibretasPorNombre(nombre);
        return ResponseEntity.ok(libretas);
    }

    @GetMapping
    public ResponseEntity<List<LibretaDTO>> obtenerTodasLasLibretas() {
        List<LibretaDTO> libretas = libretaService.obtenerTodasLasLibretas();
        return ResponseEntity.ok(libretas);
    }

}
