package com.example.demo.services;

import com.example.demo.domain.dto.DatosDetallesEvento;
import com.example.demo.domain.dto.DatosRegistroEvento;
import com.example.demo.domain.entity.Evento;

import java.time.LocalDate;
import java.util.List;

public interface EventoService {

    void registrarEvento(DatosRegistroEvento datos);

    List<DatosDetallesEvento> obtenerEventosPorUsuario();

    void eliminarEvento(Long eventoId);

    void actualizarFechaEvento(Long eventoId, LocalDate nuevaFecha);

    void actualizarEstadoEvento(Long eventoId, boolean completado);

}
