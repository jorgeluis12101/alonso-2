package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DatosRegistroEvento {
    private String titulo;
    private String contenido;
    private LocalDate fecha;  // Formato "yyyy-MM-dd"
    private String prioridad;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime recordatorio;
    private Long categoriaId;
}
