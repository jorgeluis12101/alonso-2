package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaConEventosDTO {
    private Long id;
    private String nombre;
    private List<EventoDTO> eventos;
}
