package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private String imagenUrl;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Long libretaId;
    private Long usuarioId;
    private String prioridad;
    private String categoria;
}
