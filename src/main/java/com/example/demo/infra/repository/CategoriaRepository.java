package com.example.demo.infra.repository;

import com.example.demo.domain.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT c FROM Categoria c WHERE c.usuario.id = :usuarioId")
    List<Categoria> findCategoriasByUsuarioId(@Param("usuarioId") Long usuarioId);
}