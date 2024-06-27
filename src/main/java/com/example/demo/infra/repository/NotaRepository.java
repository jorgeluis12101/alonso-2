package com.example.demo.infra.repository;

import com.example.demo.domain.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {

    List<Nota> findByUsuarioIdAndLibretaIsNull(Long usuarioId);

    ///////////////////////
    List<Nota> findByUsuarioId(Long usuarioId);

    List<Nota> findByLibretaId(Long libretaId);

    List<Nota> findByTituloContainingIgnoreCaseAndLibretaId(String titulo, Long libretaId);

    List<Nota> findByTituloContainingIgnoreCaseAndLibretaIdAndFechaCreacionBetween(String titulo, Long libretaId, LocalDateTime startDate, LocalDateTime endDate);
}
