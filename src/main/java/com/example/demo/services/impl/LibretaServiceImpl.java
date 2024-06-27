package com.example.demo.services.impl;

import com.example.demo.domain.dto.LibretaDTO;
import com.example.demo.domain.entity.Libreta;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infra.repository.LibretaRepository;
import com.example.demo.infra.repository.UsuarioRepository;
import com.example.demo.services.LibretaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibretaServiceImpl implements LibretaService {

    @Autowired
    private LibretaRepository libretaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public LibretaDTO registrarLibreta(String nombre) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));


        Optional<Libreta> libretaExistente = libretaRepository.findByNombreAndUsuarioId(nombre, usuario.getId());
        if (libretaExistente.isPresent()) {
            throw new RuntimeException("Ya existe una libreta con este nombre.");
        }

        Libreta libreta = new Libreta();
        libreta.setNombre(nombre);
        libreta.setUsuario(usuario);

        Libreta savedLibreta = libretaRepository.save(libreta);
        return convertToDTO(savedLibreta);
    }

    @Override
    public List<LibretaDTO> obtenerLibretasPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        return libretaRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarLibreta(Long libretaId) {
        Libreta libreta = libretaRepository.findById(libretaId)
                .orElseThrow(() -> new EntityNotFoundException("Libreta no encontrada: " + libretaId));
        libretaRepository.delete(libreta);
    }

    @Override
    public LibretaDTO obtenerLibretaPorId(Long libretaId) {
        Libreta libreta = libretaRepository.findById(libretaId)
                .orElseThrow(() -> new EntityNotFoundException("Libreta no encontrada: " + libretaId));
        return convertToDTO(libreta);
    }

    @Override
    public List<LibretaDTO> buscarLibretasPorNombre(String nombre) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Libreta> libretas = libretaRepository.findByNombreContainingIgnoreCaseAndUsuarioId(nombre, usuario.getId());
        return libretas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private LibretaDTO convertToDTO(Libreta libreta) {
        return new LibretaDTO(
                libreta.getId(),
                libreta.getNombre(),
                libreta.getFechaCreacion(),
                libreta.getFechaActualizacion()
        );
    }


    @Override
    public List<LibretaDTO> obtenerTodasLasLibretas() {
        List<Libreta> libretas = libretaRepository.findAll();
        return libretas.stream()
                .map(libreta -> new LibretaDTO(libreta.getId(), libreta.getNombre(), null, null))
                .collect(Collectors.toList());
    }

}
