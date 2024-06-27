// CategoriaServiceImpl.java
package com.example.demo.services.impl;

import com.example.demo.domain.dto.CategoriaConEventosDTO;
import com.example.demo.domain.dto.CategoriaDTO;
import com.example.demo.domain.dto.EventoDTO;
import com.example.demo.domain.entity.Categoria;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infra.repository.CategoriaRepository;
import com.example.demo.infra.repository.UsuarioRepository;
import com.example.demo.services.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        Categoria categoria = Categoria.builder()
                .nombre(categoriaDTO.getNombre())
                .usuario(usuario)
                .build();

        Categoria savedCategoria = categoriaRepository.save(categoria);
        return convertToDTO(savedCategoria);
    }

    @Override
    public List<CategoriaDTO> obtenerCategorias() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Categoria> categorias = categoriaRepository.findCategoriasByUsuarioId(usuario.getId());
        return categorias.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + id));
        return convertToDTO(categoria);
    }

    @Override
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + id));
        categoria.setNombre(categoriaDTO.getNombre());
        Categoria updatedCategoria = categoriaRepository.save(categoria);
        return convertToDTO(updatedCategoria);
    }

    @Override
    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + id));
        categoriaRepository.delete(categoria);
    }

    @Override
    public List<CategoriaConEventosDTO> obtenerCategoriasConEventos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Categoria> categorias = categoriaRepository.findCategoriasByUsuarioId(usuario.getId());
        return categorias.stream().map(this::convertToCategoriaConEventosDTO).collect(Collectors.toList());
    }

    @Override
    public CategoriaConEventosDTO obtenerCategoriaConEventosPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + id));
        return convertToCategoriaConEventosDTO(categoria);
    }

    private CategoriaDTO convertToDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNombre());
    }

    @Override
    public CategoriaConEventosDTO convertToCategoriaConEventosDTO(Categoria categoria) {
        List<EventoDTO> eventoDTOs = categoria.getEventos().stream()
                .map(evento -> new EventoDTO(
                        evento.getId(),
                        evento.getTitulo(),
                        evento.getContenido(),
                        evento.getFecha() != null ? evento.getFecha().toString() : null,
                        evento.getPrioridad(),
                        evento.isCompletado()
                ))
                .collect(Collectors.toList());

        return new CategoriaConEventosDTO(categoria.getId(), categoria.getNombre(), eventoDTOs);
    }
}
