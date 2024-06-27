package com.example.demo.services;

import com.example.demo.domain.dto.NotaDTO;
import com.example.demo.domain.entity.Nota;

import java.util.List;

public interface NotaService {

    ///////
    NotaDTO asignarLibreta(Long notaId, Long libretaId);

    NotaDTO registrarNotaSinLibreta(String titulo, String contenido, String prioridad, String categoria);

    List<NotaDTO> obtenerNotasPorUsuarioSinLibreta();

    NotaDTO actualizarNotaSinLibreta(Long id, NotaDTO notaDTO);

    void eliminarNotaSinLibreta(Long id);

    List<NotaDTO> filtrarNotas(String categoria, String prioridad);



    ////////////////////////////

    NotaDTO registrarNota(String titulo, String contenido, Long libretaId);

    List<NotaDTO> obtenerNotasPorUsuario();

    void eliminarNota(Long notaId);

    NotaDTO obtenerNotaPorId(Long notaId);

    List<NotaDTO> obtenerNotasPorLibreta(Long libretaId);

    Nota obtenerNotaEntidadPorId(Long notaId);

    void actualizarNota(Nota nota);

    NotaDTO actualizarNota(Long id, NotaDTO notaDTO);

    List<NotaDTO> buscarNotas(String keyword, String startDate, String endDate, Long libretaId);
}
