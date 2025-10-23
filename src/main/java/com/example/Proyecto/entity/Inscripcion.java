package com.example.Proyecto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa una Inscripción de un usuario a un curso.
 * Esta clase se mapea a la tabla "inscripciones" en la base de datos.
 * Actúa como tabla intermedia entre Usuario y Curso, pero con información adicional (fecha).
 */
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "inscripciones") // Define el nombre de la tabla en la base de datos
@Getter // Lombok: Genera automáticamente los getters para todos los campos
@Setter // Lombok: Genera automáticamente los setters para todos los campos
public class Inscripcion {
    
    /**
     * Identificador único de la inscripción.
     * Se genera automáticamente con estrategia de auto-incremento.
     */
    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el ID automáticamente (auto-increment en BD)
    private Long id;

    /**
     * Usuario que se ha inscrito en el curso.
     * Relación Many-to-One: Muchas inscripciones pueden pertenecer a un usuario.
     * Esta es la parte "muchos" de la relación.
     */
    @ManyToOne // Relación muchos a uno: muchas inscripciones pertenecen a un usuario
    @JoinColumn(name = "usuario_id") // Define el nombre de la columna FK en la tabla inscripciones
    private Usuario usuario;

    /**
     * Curso en el que se ha inscrito el usuario.
     * Relación Many-to-One: Muchas inscripciones pueden pertenecer a un curso.
     * Esta es la parte "muchos" de la relación.
     */
    @ManyToOne // Relación muchos a uno: muchas inscripciones pertenecen a un curso
    @JoinColumn(name = "curso_id") // Define el nombre de la columna FK en la tabla inscripciones
    private Curso curso;

    /**
     * Fecha y hora en que se realizó la inscripción.
     * Se inicializa automáticamente con la fecha/hora actual al crear el objeto.
     */
    private LocalDateTime fechaInscripcion = LocalDateTime.now();
}
