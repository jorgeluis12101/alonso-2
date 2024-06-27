package com.example.demo.infra.repository;

import com.example.demo.domain.entity.Libreta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibretaRepository extends JpaRepository<Libreta, Long> {
    List<Libreta> findByUsuarioId(Long usuarioId);
    Optional<Libreta> findByNombreAndUsuarioId(String nombre, Long usuarioId);
    List<Libreta> findByNombreContainingIgnoreCaseAndUsuarioId(String nombre, Long usuarioId);
}
