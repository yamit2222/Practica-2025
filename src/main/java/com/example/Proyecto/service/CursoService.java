package com.example.Proyecto.service;

import com.example.Proyecto.entity.Curso;
import com.example.Proyecto.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {
    
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    /**
     * Obtiene todos los cursos disponibles
     */
    public List<Curso> listarTodosLosCursos() {
        return cursoRepository.findAll();
    }

    /**
     * Busca un curso por su ID
     */
    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    /**
     * Obtiene un curso por su ID o lanza excepción
     */
    public Curso obtenerPorId(Long id) {
        return cursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo curso
     */
    public Curso crearCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    /**
     * Actualiza un curso existente
     */
    public Curso actualizarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    /**
     * Elimina un curso por su ID
     */
    public void eliminarCurso(Long id) {
        cursoRepository.deleteById(id);
    }
}
