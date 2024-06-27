package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

public class DatosDetallesEvento {
    private Long id;
    private String titulo;
    private String contenido;
    private LocalDate fecha; // Formato "yyyy-MM-dd"
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String prioridad;
    private boolean completado; // Este campo debe estar presente
    private LocalDateTime recordatorio;
    private String usuario;
    private String categoria;


    public DatosDetallesEvento(Long id, String titulo, String contenido, LocalDate fecha, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, String prioridad, boolean completado, LocalDateTime recordatorio, String usuario, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.prioridad = prioridad;
        this.completado = completado;
        this.recordatorio = recordatorio;
        this.usuario = usuario;
        this.categoria = categoria;
    }
}
