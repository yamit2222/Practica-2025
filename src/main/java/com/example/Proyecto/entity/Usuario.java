package com.example.Proyecto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

/**
 * Entidad que representa un Usuario del sistema.
 * Esta clase se mapea a la tabla "usuarios" en la base de datos.
 * Los usuarios pueden autenticarse y inscribirse en cursos.
 */
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "usuarios") // Define el nombre de la tabla en la base de datos
@Getter // Lombok: Genera automáticamente los getters para todos los campos
@Setter // Lombok: Genera automáticamente los setters para todos los campos
public class Usuario {
    
    /**
     * Identificador único del usuario.
     * Se genera automáticamente con estrategia de auto-incremento.
     */
    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el ID automáticamente (auto-increment en BD)
    private Long id;

    /**
     * Nombre de usuario único para autenticación.
     * Debe ser único en todo el sistema y no puede estar vacío.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio") // Validación: no puede ser null, vacío o solo espacios
    @Column(unique = true) // Restricción en BD: debe ser único
    private String username;

    /**
     * Contraseña del usuario (debe estar encriptada).
     * Se almacena encriptada usando BCrypt u otro algoritmo de hash seguro.
     */
    @NotBlank(message = "La contraseña es obligatoria") // Validación: no puede ser null, vacío o solo espacios
    private String password;

    /**
     * Correo electrónico del usuario.
     * Debe tener formato válido de email y ser único en el sistema.
     */
    @Email(message = "Correo inválido") // Validación: debe tener formato de email válido
    @NotBlank(message = "El correo es obligatorio") // Validación: no puede ser null, vacío o solo espacios
    @Column(unique = true) // Restricción en BD: debe ser único
    private String email;

    /**
     * Conjunto de cursos en los que el usuario está inscrito.
     * Relación Many-to-Many: Un usuario puede estar inscrito en muchos cursos.
     * - mappedBy: Indica que la relación es bidireccional y está mapeada por el campo "usuarios" en Curso
     */
    @ManyToMany(mappedBy = "usuarios")
    private Set<Curso> cursosInscritos;
}
