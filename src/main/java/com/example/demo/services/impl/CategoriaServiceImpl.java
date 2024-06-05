package com.example.demo.services.impl;

import com.example.demo.domain.dto.CategoriaConEventosDTO;
import com.example.demo.domain.dto.CategoriaDTO;
import com.example.demo.domain.dto.EventoDTO;
import com.example.demo.domain.entity.Categoria;
import com.example.demo.domain.entity.Evento;
import com.example.demo.infra.repository.CategoriaRepository;
import com.example.demo.infra.repository.EventoRepository;
import com.example.demo.services.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertToEntity(categoriaDTO);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return convertToDTO(savedCategoria);
    }

    @Override
    public List<CategoriaDTO> obtenerCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
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
        List<Categoria> categorias = categoriaRepository.findAll();
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
                        evento.getFecha() != null ? evento.getFecha().toString() : null, // Comprobación nula para fecha
                        evento.getPrioridad(),
                        evento.isCompletado() // Asegúrate de que este campo se mapee correctamente
                ))
                .collect(Collectors.toList());

        return new CategoriaConEventosDTO(categoria.getId(), categoria.getNombre(), eventoDTOs);
    }

    private Categoria convertToEntity(CategoriaDTO categoriaDTO) {
        return new Categoria(categoriaDTO.getId(), categoriaDTO.getNombre(), null);
    }

    public void marcarComoCompletado(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado: " + id));
        evento.setCompletado(true);
        eventoRepository.save(evento);
    }
}
