package com.example.demo.services;

import com.example.demo.domain.dto.CategoriaConEventosDTO;
import com.example.demo.domain.dto.CategoriaDTO;
import com.example.demo.domain.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO);
    List<CategoriaDTO> obtenerCategorias();
    CategoriaDTO obtenerCategoriaPorId(Long id);
    CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO);
    void eliminarCategoria(Long id);
    List<CategoriaConEventosDTO> obtenerCategoriasConEventos();
    CategoriaConEventosDTO obtenerCategoriaConEventosPorId(Long id);
    CategoriaConEventosDTO convertToCategoriaConEventosDTO(Categoria categoria); // Asegúrate de que este método esté aquí
}
