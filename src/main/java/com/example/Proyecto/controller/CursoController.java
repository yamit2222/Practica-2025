package com.example.Proyecto.controller;

import com.example.Proyecto.entity.Curso;
import com.example.Proyecto.entity.Inscripcion;
import com.example.Proyecto.entity.Usuario;
import com.example.Proyecto.repository.CursoRepository;
import com.example.Proyecto.repository.InscripcionRepository;
import com.example.Proyecto.repository.UsuarioRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InscripcionRepository inscripcionRepository;

    public CursoController(CursoRepository cursoRepository, UsuarioRepository usuarioRepository, InscripcionRepository inscripcionRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    @GetMapping
    public String listarCursos(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Curso> cursos = cursoRepository.findAll();
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        model.addAttribute("cursos", cursos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("inscripciones", inscripcionRepository.findByUsuario(usuario));
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
        cursoRepository.save(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}")
    public String detalleCurso(@PathVariable Long id, Model model) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        if (cursoOpt.isEmpty()) {
            return "redirect:/cursos";
        }
        Curso curso = cursoOpt.get();
        model.addAttribute("curso", curso);
        model.addAttribute("inscripciones", inscripcionRepository.findByCurso(curso));
        return "curso_detalle";
    }

    @PostMapping("/{id}/inscribir")
    public String inscribirUsuario(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Curso curso = cursoRepository.findById(id).orElseThrow();

        // Verifica si ya está inscrito o capacidad completa
        long inscritos = inscripcionRepository.findByCurso(curso).size();
        boolean yaInscrito = inscripcionRepository.findByUsuarioAndCurso(usuario, curso).isPresent();

        if (!yaInscrito && inscritos < curso.getCapacidad()) {
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setUsuario(usuario);
            inscripcion.setCurso(curso);
            inscripcion.setFechaInscripcion(LocalDateTime.now());
            inscripcionRepository.save(inscripcion);
        }
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/remover")
    public String removerInscripcion(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Curso curso = cursoRepository.findById(id).orElseThrow();

        inscripcionRepository.deleteByUsuarioAndCurso(usuario, curso);

        return "redirect:/cursos";
    }

    @GetMapping("/{id}/editar")
    public String editarCursoForm(@PathVariable Long id, Model model) {
        Curso curso = cursoRepository.findById(id).orElseThrow();
        model.addAttribute("curso", curso);
        return "curso_form";
    }

    @PostMapping("/{id}/editar")
    public String actualizarCurso(@PathVariable Long id, @Valid @ModelAttribute Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return "curso_form";
        }
        curso.setId(id);
        cursoRepository.save(curso);
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return "redirect:/cursos";
    }
}
