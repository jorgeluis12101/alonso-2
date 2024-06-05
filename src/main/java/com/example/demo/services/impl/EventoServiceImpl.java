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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void registrarEvento(DatosRegistroEvento datos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        Categoria categoria = categoriaRepository.findById(datos.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + datos.getCategoriaId()));

        Evento evento = new Evento();
        evento.setTitulo(datos.getTitulo());
        evento.setContenido(datos.getContenido());
        evento.setFecha(datos.getFecha());
        evento.setPrioridad(datos.getPrioridad());
        evento.setRecordatorio(datos.getRecordatorio());
        evento.setUsuario(usuario);
        evento.setCategoria(categoria);
        evento.setCompletado(false); // Por defecto, el evento está pendiente

        eventoRepository.save(evento);
    }

    @Override
    public List<DatosDetallesEvento> obtenerEventosPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
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
        evento.setFechaActualizacion(LocalDateTime.now()); // Registrar la fecha de modificación
        eventoRepository.save(evento);
    }

    @Override
    public void actualizarEstadoEvento(Long eventoId, boolean completado) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado: " + eventoId));
        evento.setCompletado(completado);
        evento.setFechaActualizacion(LocalDateTime.now()); // Registrar la fecha de modificación
        eventoRepository.save(evento);
    }

    @Override
    public Evento obtenerEventoPorId(Long eventoId) {
        return eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado: " + eventoId));
    }


}
