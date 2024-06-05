package com.example.demo.services;

import com.example.demo.domain.entity.Role;
import com.example.demo.domain.entity.Usuario;
import com.example.demo.infra.repository.UsuarioRepository;
import com.example.demo.infra.security.JwtService;
import com.example.demo.infra.security.LoginRequest;
import com.example.demo.infra.security.TokenResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Usuario user=usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user, user);
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    public TokenResponse addUsuario(Usuario usuario) {
        Usuario user = Usuario.builder()
                .username(usuario.getUsername())
                .password(passwordEncoder.encode(usuario.getPassword()))
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .telefono(usuario.getTelefono())
                .correo(usuario.getCorreo())
                .dni(usuario.getDni())
                .role(Role.USER)
                .build();

        usuarioRepository.save(user);

        String token = jwtService.getToken(user, user);
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public void cambiarRolUsuario(Long usuarioId, Role nuevoRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRole(nuevoRol);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void updateUsuario(Usuario usuario, Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado")
        );

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setDni(usuario.getDni());
        usuarioExistente.setRole(usuario.getRole());

        usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}
