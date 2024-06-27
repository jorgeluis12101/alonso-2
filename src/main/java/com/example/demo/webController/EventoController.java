// EventoController.java
package com.example.demo.webController;

import com.example.demo.domain.dto.DatosDetallesEvento;
import com.example.demo.domain.dto.DatosRegistroEvento;
import com.example.demo.services.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEvento(@RequestBody DatosRegistroEvento datos) {
        try {
            eventoService.registrarEvento(datos);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<DatosDetallesEvento>> listarEventos() {
        try {
            List<DatosDetallesEvento> eventos = eventoService.obtenerEventosPorUsuario();
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEvento(@PathVariable Long id) {
        try {
            eventoService.eliminarEvento(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/actualizar-estado/{eventoId}")
    public ResponseEntity<?> actualizarEstadoEvento(@PathVariable Long eventoId, @RequestBody Map<String, Boolean> nuevoEstadoMap) {
        try {
            boolean nuevoEstado = nuevoEstadoMap.get("completado");
            eventoService.actualizarEstadoEvento(eventoId, nuevoEstado);
            return ResponseEntity.ok(Collections.singletonMap("message", "Estado actualizado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("message", "Estado inválido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PutMapping("/actualizar-fecha/{eventoId}")
    public ResponseEntity<?> actualizarFechaEvento(@PathVariable Long eventoId, @RequestBody Map<String, String> nuevaFechaMap) {
        try {
            LocalDate nuevaFecha = LocalDate.parse(nuevaFechaMap.get("fecha"));
            eventoService.actualizarFechaEvento(eventoId, nuevaFecha);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
