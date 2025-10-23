package com.example.Proyecto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un Curso en el sistema.
 * Esta clase se mapea a la tabla "cursos" en la base de datos.
 */
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "cursos") // Define el nombre de la tabla en la base de datos
@Getter // Lombok: Genera automáticamente los getters para todos los campos
@Setter // Lombok: Genera automáticamente los setters para todos los campos
public class Curso {
    
    /**
     * Identificador único del curso.
     * Se genera automáticamente con estrategia de auto-incremento.
     */
    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el ID automáticamente (auto-increment en BD)
    private Long id;

    /**
     * Nombre del curso.
     * Campo obligatorio que no puede estar vacío.
     */
    @NotBlank(message = "El nombre del curso es obligatorio") // Validación: no puede ser null, vacío o solo espacios
    private String nombre;

    /**
     * Nombre del instructor que imparte el curso.
     * Campo obligatorio que no puede estar vacío.
     */
    @NotBlank(message = "El instructor es obligatorio") // Validación: no puede ser null, vacío o solo espacios
    private String instructor;

    /**
     * Capacidad máxima de estudiantes que pueden inscribirse en el curso.
     * Debe ser al menos 1.
     */
    @Min(value = 1, message = "La capacidad debe ser al menos 1") // Validación: mínimo valor de 1
    private int capacidad;

    /**
     * Conjunto de inscripciones asociadas a este curso.
     * Relación One-to-Many: Un curso puede tener muchas inscripciones.
     * - mappedBy: Indica que la relación es bidireccional y está mapeada por el campo "curso" en Inscripcion
     * - cascade: Las operaciones en Curso se propagan a las Inscripciones
     * - orphanRemoval: Si una inscripción se elimina de este Set, se elimina de la BD
     */
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inscripcion> inscripciones = new HashSet<>();

    /**
     * Conjunto de usuarios inscritos en este curso.
     * Relación Many-to-Many: Un curso puede tener muchos usuarios y un usuario puede estar en muchos cursos.
     * Se crea una tabla intermedia "curso_usuario" para gestionar esta relación.
     */
    @ManyToMany
    @JoinTable(
        name = "curso_usuario", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "curso_id"), // Columna que hace referencia a la tabla cursos
        inverseJoinColumns = @JoinColumn(name = "usuario_id") // Columna que hace referencia a la tabla usuarios
    )
    private Set<Usuario> usuarios = new HashSet<>();
}
