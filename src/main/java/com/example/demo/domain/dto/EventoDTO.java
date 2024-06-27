package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventoDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private String fecha;
    private String prioridad;
    private boolean completado;
}
