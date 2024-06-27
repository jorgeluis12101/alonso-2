package com.example.demo.services.impl;

import com.example.demo.domain.dto.NotaDTO;
import com.example.demo.domain.entity.Libreta;
import com.example.demo.domain.entity.Nota;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infra.repository.LibretaRepository;
import com.example.demo.infra.repository.NotaRepository;
import com.example.demo.infra.repository.UsuarioRepository;
import com.example.demo.services.NotaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotaServiceImpl implements NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibretaRepository libretaRepository;

    @Override
    public NotaDTO registrarNotaSinLibreta(String titulo, String contenido, String prioridad, String categoria) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        Nota nota = new Nota();
        nota.setTitulo(titulo);
        nota.setContenido(contenido);
        nota.setPrioridad(prioridad);
        nota.setCategoria(categoria);
        nota.setUsuario(usuario);
        nota.setLibreta(null); // Asegurarse de que libreta es nulo

        Nota savedNota = notaRepository.save(nota);
        return convertToDTO(savedNota);
    }


    @Override
    public List<NotaDTO> obtenerNotasPorUsuarioSinLibreta() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Nota> notas = notaRepository.findByUsuarioIdAndLibretaIsNull(usuario.getId());
        return notas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<NotaDTO> filtrarNotas(String categoria, String prioridad) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Nota> notas = notaRepository.findByUsuarioId(usuario.getId());

        if (categoria != null && !categoria.isEmpty()) {
            notas = notas.stream()
                    .filter(nota -> categoria.equals(nota.getCategoria()))
                    .collect(Collectors.toList());
        }

        if (prioridad != null && !prioridad.isEmpty()) {
            notas = notas.stream()
                    .filter(nota -> prioridad.equals(nota.getPrioridad()))
                    .collect(Collectors.toList());
        }

        return notas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    // NotaServiceImpl.java
    @Override
    public NotaDTO actualizarNotaSinLibreta(Long id, NotaDTO notaDTO) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada: " + id));

        nota.setTitulo(notaDTO.getTitulo());
        nota.setContenido(notaDTO.getContenido());
        nota.setFechaActualizacion(LocalDateTime.now());

        Nota notaActualizada = notaRepository.save(nota);
        return convertToDTO(notaActualizada);
    }

    @Override
    public void eliminarNotaSinLibreta(Long notaId) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada: " + notaId));
        notaRepository.delete(nota);
    }

    /////////////////////////////
    @Override
    public NotaDTO registrarNota(String titulo, String contenido, Long libretaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        Libreta libreta = libretaRepository.findById(libretaId)
                .orElseThrow(() -> new RuntimeException("Libreta no encontrada: " + libretaId));

        Nota nota = new Nota();
        nota.setTitulo(titulo);
        nota.setContenido(contenido);
        nota.setUsuario(usuario);
        nota.setLibreta(libreta);

        Nota savedNota = notaRepository.save(nota);
        return convertToDTO(savedNota);
    }

    @Override
    public List<NotaDTO> obtenerNotasPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        return notaRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarNota(Long notaId) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada: " + notaId));
        notaRepository.delete(nota);
    }

    @Override
    public NotaDTO obtenerNotaPorId(Long notaId) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada: " + notaId));
        return convertToDTO(nota);
    }

    @Override
    public List<NotaDTO> obtenerNotasPorLibreta(Long libretaId) {
        return notaRepository.findByLibretaId(libretaId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Nota obtenerNotaEntidadPorId(Long notaId) {
        return notaRepository.findById(notaId)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada: " + notaId));
    }

    @Override
    public void actualizarNota(Nota nota) {
        notaRepository.save(nota);
    }

    @Override
    public NotaDTO actualizarNota(Long id, NotaDTO notaDTO) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada: " + id));

        nota.setTitulo(notaDTO.getTitulo());
        nota.setContenido(notaDTO.getContenido());
        nota.setFechaActualizacion(LocalDateTime.now());

        Nota notaActualizada = notaRepository.save(nota);
        return convertToDTO(notaActualizada);
    }

    @Override
    public List<NotaDTO> buscarNotas(String keyword, String startDate, String endDate, Long libretaId) {
        List<Nota> notas = new ArrayList<>();

        LocalDateTime start = (startDate != null) ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;

        if (start != null && end != null) {
            notas.addAll(notaRepository.findByTituloContainingIgnoreCaseAndLibretaIdAndFechaCreacionBetween(
                    keyword, libretaId, start, end));
        } else {
            notas.addAll(notaRepository.findByTituloContainingIgnoreCaseAndLibretaId(
                    keyword, libretaId));
        }

        return notas.stream().map(this::convertToDTO).distinct().collect(Collectors.toList());
    }

    public NotaDTO convertToDTO(Nota nota) {
        NotaDTO notaDTO = new NotaDTO();
        notaDTO.setId(nota.getId());
        notaDTO.setTitulo(nota.getTitulo());
        notaDTO.setContenido(nota.getContenido());
        notaDTO.setFechaCreacion(nota.getFechaCreacion());
        notaDTO.setFechaActualizacion(nota.getFechaActualizacion());
        notaDTO.setImagenUrl(nota.getImagenUrl());
        notaDTO.setUsuarioId(nota.getUsuario().getId());
        notaDTO.setPrioridad(nota.getPrioridad());
        notaDTO.setCategoria(nota.getCategoria());

        if (nota.getLibreta() != null) {
            notaDTO.setLibretaId(nota.getLibreta().getId());
        } else {
            notaDTO.setLibretaId(null);
        }

        return notaDTO;
    }
    @Override
    public NotaDTO asignarLibreta(Long notaId, Long libretaId) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada: " + notaId));

        Libreta libreta = libretaRepository.findById(libretaId)
                .orElseThrow(() -> new EntityNotFoundException("Libreta no encontrada: " + libretaId));

        nota.setLibreta(libreta);
        Nota notaActualizada = notaRepository.save(nota);
        return convertToDTO(notaActualizada);
    }

}
