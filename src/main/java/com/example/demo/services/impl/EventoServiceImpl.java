// EventoServiceImpl.java
package com.example.demo.services.impl;

import com.example.demo.domain.dto.DatosDetallesEvento;
import com.example.demo.domain.dto.DatosRegistroEvento;
import com.example.demo.domain.entity.Categoria;
import com.example.demo.domain.entity.Evento;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infra.repository.CategoriaRepository;
import com.example.demo.infra.repository.EventoRepository;
import com.example.demo.infra.repository.UsuarioRepository;
import com.example.demo.services.EventoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public void registrarEvento(DatosRegistroEvento datos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        Categoria categoria = categoriaRepository.findById(datos.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + datos.getCategoriaId()));

        Evento evento = Evento.builder()
                .titulo(datos.getTitulo())
                .contenido(datos.getContenido())
                .fecha(datos.getFecha())
                .prioridad(datos.getPrioridad())
                .recordatorio(datos.getRecordatorio())
                .usuario(usuario)
                .categoria(categoria)
                .completado(false)
                .build();

        eventoRepository.save(evento);
    }

    @Override
    public List<DatosDetallesEvento> obtenerEventosPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Evento> eventos = eventoRepository.findByUsuarioIdAndCompletadoFalse(usuario.getId());

        return eventos.stream()
                .map(evento -> new DatosDetallesEvento(
                        evento.getId(),
                        evento.getTitulo(),
                        evento.getContenido(),
                        evento.getFecha(),
                        evento.getFechaCreacion(),
                        evento.getFechaActualizacion(),
                        evento.getPrioridad(),
                        evento.isCompletado(),
                        evento.getRecordatorio(),
                        evento.getUsuario().getUsername(),
                        evento.getCategoria() != null ? evento.getCategoria().getNombre() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado: " + eventoId));
        eventoRepository.delete(evento);
    }

    @Override
    public void actualizarFechaEvento(Long eventoId, LocalDate nuevaFecha) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado: " + eventoId));
        evento.setFecha(nuevaFecha);
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoRepository.save(evento);
    }

    @Override
    public void actualizarEstadoEvento(Long eventoId, boolean completado) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado: " + eventoId));
        evento.setCompletado(completado);
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoRepository.save(evento);
    }

}
