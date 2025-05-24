package com.example.Proyecto.controller;

import com.example.Proyecto.entity.Usuario;
import com.example.Proyecto.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registroSubmit(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.usuario", "El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "El correo ya está registrado");
        }

        if (result.hasErrors()) {
            return "registro";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "redirect:/login?registroExitoso";
    }
}
