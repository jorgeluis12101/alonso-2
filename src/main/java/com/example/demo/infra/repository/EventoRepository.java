package com.example.demo.infra.repository;

import com.example.demo.domain.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByUsuarioId(Long usuarioId);
    List<Evento> findByUsuarioIdAndCompletadoFalse(Long usuarioId);
    List<Evento> findByRecordatorioBeforeAndCompletadoFalse(LocalDateTime fecha);
}