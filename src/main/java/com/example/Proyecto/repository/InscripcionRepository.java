package com.example.Proyecto.repository;

import com.example.Proyecto.entity.Inscripcion;
import com.example.Proyecto.entity.Usuario;
import com.example.Proyecto.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByUsuario(Usuario usuario);
    List<Inscripcion> findByCurso(Curso curso);
    Optional<Inscripcion> findByUsuarioAndCurso(Usuario usuario, Curso curso);
    void deleteByUsuarioAndCurso(Usuario usuario, Curso curso);
}
