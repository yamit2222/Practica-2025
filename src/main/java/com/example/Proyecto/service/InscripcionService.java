package com.example.Proyecto.service;

import com.example.Proyecto.entity.Curso;
import com.example.Proyecto.entity.Inscripcion;
import com.example.Proyecto.entity.Usuario;
import com.example.Proyecto.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscripcionService {
    
    private final InscripcionRepository inscripcionRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    /**
     * Obtiene todas las inscripciones de un usuario
     */
    public List<Inscripcion> listarInscripcionesPorUsuario(Usuario usuario) {
        return inscripcionRepository.findByUsuario(usuario);
    }

    /**
     * Obtiene todas las inscripciones de un curso
     */
    public List<Inscripcion> listarInscripcionesPorCurso(Curso curso) {
        return inscripcionRepository.findByCurso(curso);
    }

    /**
     * Cuenta el número de inscritos en un curso
     */
    public long contarInscritosPorCurso(Curso curso) {
        return inscripcionRepository.findByCurso(curso).size();
    }

    /**
     * Verifica si un usuario ya está inscrito en un curso
     */
    public boolean estaInscrito(Usuario usuario, Curso curso) {
        return inscripcionRepository.findByUsuarioAndCurso(usuario, curso).isPresent();
    }

    /**
     * Verifica si un curso tiene capacidad disponible
     */
    public boolean tieneCapacidadDisponible(Curso curso) {
        long inscritos = contarInscritosPorCurso(curso);
        return inscritos < curso.getCapacidad();
    }

    /**
     * Inscribe a un usuario en un curso validando las condiciones
     */
    @Transactional
    public Inscripcion inscribirUsuario(Usuario usuario, Curso curso) {
        // Validación: ya está inscrito
        if (estaInscrito(usuario, curso)) {
            throw new RuntimeException("El usuario ya está inscrito en este curso");
        }

        // Validación: capacidad completa
        if (!tieneCapacidadDisponible(curso)) {
            throw new RuntimeException("El curso ha alcanzado su capacidad máxima");
        }

        // Crear y guardar la inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuario(usuario);
        inscripcion.setCurso(curso);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        
        return inscripcionRepository.save(inscripcion);
    }

    /**
     * Remueve la inscripción de un usuario en un curso
     */
    @Transactional
    public void removerInscripcion(Usuario usuario, Curso curso) {
        if (!estaInscrito(usuario, curso)) {
            throw new RuntimeException("El usuario no está inscrito en este curso");
        }
        inscripcionRepository.deleteByUsuarioAndCurso(usuario, curso);
    }
}
