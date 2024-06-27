package com.example.demo.webController;

import com.example.demo.domain.dto.LibretaDTO;
import com.example.demo.domain.dto.NotaDTO;
import com.example.demo.domain.entity.Libreta;
import com.example.demo.domain.entity.Nota;
import com.example.demo.services.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    @Value("${upload.path}")
    private String uploadPath;

    // NotaController.java
    @PostMapping("/crear-sin-libreta")
    public ResponseEntity<NotaDTO> crearNotaSinLibreta(@RequestBody NotaDTO notaDTO) {
        NotaDTO nuevaNota = notaService.registrarNotaSinLibreta(notaDTO.getTitulo(), notaDTO.getContenido(), notaDTO.getPrioridad(), notaDTO.getCategoria());
        return ResponseEntity.ok(nuevaNota);
    }

    @GetMapping("/usuario-sin-libreta")
    public ResponseEntity<List<NotaDTO>> obtenerNotasPorUsuarioSinLibreta() {
        List<NotaDTO> notas = notaService.obtenerNotasPorUsuarioSinLibreta();
        return ResponseEntity.ok(notas);
    }

    @PutMapping("/asignar-libreta/{notaId}")
    public ResponseEntity<NotaDTO> asignarLibreta(@PathVariable Long notaId, @RequestParam Long libretaId) {
        NotaDTO notaActualizada = notaService.asignarLibreta(notaId, libretaId);
        return ResponseEntity.ok(notaActualizada);
    }

    // NotaController.java
    @PutMapping("/actualizar-sin-libreta/{id}")
    public ResponseEntity<NotaDTO> actualizarNotaSinLibreta(@PathVariable Long id, @RequestBody NotaDTO notaDTO) {
        NotaDTO notaActualizada = notaService.actualizarNotaSinLibreta(id, notaDTO);
        return ResponseEntity.ok(notaActualizada);
    }

    @DeleteMapping("/eliminar-sin-libreta/{id}")
    public ResponseEntity<Void> eliminarNotaSinLibreta(@PathVariable Long id) {
        notaService.eliminarNotaSinLibreta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<NotaDTO>> filtrarNotas(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String prioridad) {
        List<NotaDTO> notas = notaService.filtrarNotas(categoria, prioridad);
        return ResponseEntity.ok(notas);
    }

    /////////////////////
    @PostMapping("/crear")
    public ResponseEntity<NotaDTO> crearNota(@RequestBody Nota nota) {
        NotaDTO nuevaNota = notaService.registrarNota(nota.getTitulo(), nota.getContenido(), nota.getLibreta().getId());
        return ResponseEntity.ok(nuevaNota);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaDTO> obtenerNota(@PathVariable Long id) {
        NotaDTO nota = notaService.obtenerNotaPorId(id);
        if (nota != null) {
            return ResponseEntity.ok(nota);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<NotaDTO>> obtenerNotasPorUsuario() {
        List<NotaDTO> notas = notaService.obtenerNotasPorUsuario();
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/libreta/{libretaId}")
    public ResponseEntity<List<NotaDTO>> obtenerNotasPorLibreta(@PathVariable Long libretaId) {
        List<NotaDTO> notas = notaService.obtenerNotasPorLibreta(libretaId);
        return ResponseEntity.ok(notas);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarNota(@PathVariable Long id) {
        notaService.eliminarNota(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String imageUrl = null;
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + file.getOriginalFilename());
                Files.write(path, bytes);
                imageUrl = "http://localhost:8080/uploads/" + file.getOriginalFilename();
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(null);
            }
        }

        Nota nota = notaService.obtenerNotaEntidadPorId(id);
        nota.setImagenUrl(imageUrl);
        notaService.actualizarNota(nota);

        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<NotaDTO> actualizarNota(@PathVariable Long id, @RequestBody NotaDTO notaDTO) {
        NotaDTO notaActualizada = notaService.actualizarNota(id, notaDTO);
        return ResponseEntity.ok(notaActualizada);
    }

    @GetMapping("/buscar")
    public List<NotaDTO> buscarNotas(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam Long libretaId) {
        return notaService.buscarNotas(keyword, startDate, endDate, libretaId);
    }
}
