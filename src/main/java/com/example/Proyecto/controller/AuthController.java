package com.example.Proyecto.controller;

import com.example.Proyecto.entity.Usuario;
import com.example.Proyecto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
    public String registroSubmit(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result) {
        // Validaciones de negocio delegadas al servicio
        if (usuarioService.existeUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.usuario", "El nombre de usuario ya existe");
        }
        
        if (usuarioService.existeEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "El correo ya está registrado");
        }

        if (result.hasErrors()) {
            return "registro";
        }

        // La lógica de encriptación y guardado está en el servicio
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registroExitoso";
    }
}
