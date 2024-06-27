package com.example.demo.services;

import com.example.demo.domain.dto.LibretaDTO;
import com.example.demo.domain.entity.Libreta;

import java.util.List;

public interface LibretaService {

    List<LibretaDTO> obtenerTodasLasLibretas();

    LibretaDTO registrarLibreta(String nombre);

    List<LibretaDTO> obtenerLibretasPorUsuario();

    void eliminarLibreta(Long libretaId);

    LibretaDTO obtenerLibretaPorId(Long libretaId);

    List<LibretaDTO> buscarLibretasPorNombre(String nombre);
}
