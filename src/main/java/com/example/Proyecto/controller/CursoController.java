package com.example.Proyecto.controller;

import com.example.Proyecto.entity.Curso;
import com.example.Proyecto.entity.Usuario;
import com.example.Proyecto.service.CursoService;
import com.example.Proyecto.service.InscripcionService;
import com.example.Proyecto.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final UsuarioService usuarioService;
    private final InscripcionService inscripcionService;

    public CursoController(CursoService cursoService, UsuarioService usuarioService, InscripcionService inscripcionService) {
        this.cursoService = cursoService;
        this.usuarioService = usuarioService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    public String listarCursos(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Obtener datos a través de los servicios
        Usuario usuario = usuarioService.obtenerPorUsername(userDetails.getUsername());
        
        model.addAttribute("cursos", cursoService.listarTodosLosCursos());
        model.addAttribute("usuario", usuario);
        model.addAttribute("inscripciones", inscripcionService.listarInscripcionesPorUsuario(usuario));
        
        return "cursos";
    }

    @GetMapping("/nuevo")
    public String nuevoCursoForm(Model model) {
        model.addAttribute("curso", new Curso());
        return "curso_form";
    }

    @PostMapping("/nuevo")
    public String crearCurso(@Valid @ModelAttribute Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return "curso_form";
        }
        
        // La lógica de guardado está en el servicio
        cursoService.crearCurso(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}")
    public String detalleCurso(@PathVariable Long id, Model model) {
        // Buscar curso a través del servicio
        return cursoService.buscarPorId(id)
            .map(curso -> {
                model.addAttribute("curso", curso);
                model.addAttribute("inscripciones", inscripcionService.listarInscripcionesPorCurso(curso));
                return "curso_detalle";
            })
            .orElse("redirect:/cursos");
    }

    @GetMapping("/{id}/editar")
    public String editarCursoForm(@PathVariable Long id, Model model) {
        Curso curso = cursoService.obtenerPorId(id);
        model.addAttribute("curso", curso);
        return "curso_form";
    }

    @PostMapping("/{id}/editar")
    public String actualizarCurso(@PathVariable Long id, @Valid @ModelAttribute Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return "curso_form";
        }
        
        curso.setId(id);
        cursoService.actualizarCurso(curso);
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/inscribir")
    public String inscribirUsuario(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Obtener entidades a través de los servicios
            Usuario usuario = usuarioService.obtenerPorUsername(userDetails.getUsername());
            Curso curso = cursoService.obtenerPorId(id);
            
            // La lógica de inscripción (validaciones incluidas) está en el servicio
            inscripcionService.inscribirUsuario(usuario, curso);
        } catch (RuntimeException e) {
            // Manejo de errores (podría añadirse un flash attribute con el mensaje)
            // Por ahora, simplemente redirige
        }
        
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/remover")
    public String removerInscripcion(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Obtener entidades a través de los servicios
            Usuario usuario = usuarioService.obtenerPorUsername(userDetails.getUsername());
            Curso curso = cursoService.obtenerPorId(id);
            
            // La lógica de remoción está en el servicio
            inscripcionService.removerInscripcion(usuario, curso);
        } catch (RuntimeException e) {
            // Manejo de errores
        }
        
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return "redirect:/cursos";
    }
}

