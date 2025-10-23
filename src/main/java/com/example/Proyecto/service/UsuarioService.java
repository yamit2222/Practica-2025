package com.example.Proyecto.service;

import com.example.Proyecto.entity.Usuario;
import com.example.Proyecto.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verifica si un nombre de usuario ya existe
     */
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    /**
     * Verifica si un email ya existe
     */
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Registra un nuevo usuario encriptando su contraseña
     */
    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su nombre de usuario
     */
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Obtiene un usuario por su nombre de usuario o lanza excepción
     */
    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
}
