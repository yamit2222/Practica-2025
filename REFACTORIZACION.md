# Refactorización del Proyecto - Arquitectura por Capas

## 📋 Resumen de Cambios

Se ha refactorizado el proyecto para seguir las **mejores prácticas de arquitectura por capas**, separando claramente las responsabilidades de cada componente.

## 🏗️ Arquitectura Implementada

### **Capa de Presentación (Controllers)**
- **Responsabilidad**: Solo manejar peticiones HTTP y delegar la lógica de negocio
- **No debe**: Acceder directamente a repositorios ni contener validaciones de negocio

### **Capa de Negocio (Services)**
- **Responsabilidad**: Contener toda la lógica de negocio y validaciones
- **Debe**: Ser el intermediario entre controladores y repositorios

### **Capa de Acceso a Datos (Repositories)**
- **Responsabilidad**: Solo acceder a la base de datos
- **No debe**: Contener lógica de negocio

### **Capa de Entidades (Entities)**
- **Responsabilidad**: Representar el modelo de datos

---

## 📁 Estructura de Archivos

### **Services (Nueva estructura)**
```
service/
├── UsuarioService.java       ✅ Nuevo servicio completo
├── CursoService.java          ✅ Nuevo servicio completo
├── InscripcionService.java    ✅ Nuevo servicio completo
└── UserDetailsServiceImpl.java (sin cambios)
```

### **Controllers (Refactorizados)**
```
controller/
├── AuthController.java        ♻️ Refactorizado - usa UsuarioService
└── CursoController.java       ♻️ Refactorizado - usa servicios
```

---

## 🔄 Cambios Específicos

### **1. UsuarioService.java**
**Métodos implementados:**
- `existeUsername(String username)` - Validación de usuario existente
- `existeEmail(String email)` - Validación de email existente
- `registrarUsuario(Usuario usuario)` - Registro con encriptación de contraseña
- `buscarPorUsername(String username)` - Búsqueda opcional
- `obtenerPorUsername(String username)` - Búsqueda con excepción

**Antes** (en AuthController):
```java
// ❌ Controlador accediendo directamente al repositorio
usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
usuarioRepository.save(usuario);
```

**Ahora** (en UsuarioService):
```java
// ✅ Lógica en el servicio
public Usuario registrarUsuario(Usuario usuario) {
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    return usuarioRepository.save(usuario);
}
```

---

### **2. CursoService.java**
**Métodos implementados:**
- `listarTodosLosCursos()` - Listar todos los cursos
- `buscarPorId(Long id)` - Búsqueda opcional
- `obtenerPorId(Long id)` - Búsqueda con excepción
- `crearCurso(Curso curso)` - Crear nuevo curso
- `actualizarCurso(Curso curso)` - Actualizar curso
- `eliminarCurso(Long id)` - Eliminar curso

**Antes** (en CursoController):
```java
// ❌ Acceso directo al repositorio
List<Curso> cursos = cursoRepository.findAll();
```

**Ahora** (en CursoService):
```java
// ✅ Método en el servicio
public List<Curso> listarTodosLosCursos() {
    return cursoRepository.findAll();
}
```

---

### **3. InscripcionService.java**
**Métodos implementados:**
- `listarInscripcionesPorUsuario(Usuario usuario)` - Inscripciones del usuario
- `listarInscripcionesPorCurso(Curso curso)` - Inscripciones del curso
- `contarInscritosPorCurso(Curso curso)` - Contar inscritos
- `estaInscrito(Usuario usuario, Curso curso)` - Verificar inscripción
- `tieneCapacidadDisponible(Curso curso)` - Verificar capacidad
- `inscribirUsuario(Usuario usuario, Curso curso)` - **Inscripción con validaciones**
- `removerInscripcion(Usuario usuario, Curso curso)` - **Remoción con validación**

**Antes** (en CursoController):
```java
// ❌ Validaciones y lógica en el controlador
long inscritos = inscripcionRepository.findByCurso(curso).size();
boolean yaInscrito = inscripcionRepository.findByUsuarioAndCurso(usuario, curso).isPresent();

if (!yaInscrito && inscritos < curso.getCapacidad()) {
    Inscripcion inscripcion = new Inscripcion();
    inscripcion.setUsuario(usuario);
    inscripcion.setCurso(curso);
    inscripcion.setFechaInscripcion(LocalDateTime.now());
    inscripcionRepository.save(inscripcion);
}
```

**Ahora** (en InscripcionService):
```java
// ✅ Validaciones y lógica en el servicio
@Transactional
public Inscripcion inscribirUsuario(Usuario usuario, Curso curso) {
    if (estaInscrito(usuario, curso)) {
        throw new RuntimeException("El usuario ya está inscrito en este curso");
    }
    
    if (!tieneCapacidadDisponible(curso)) {
        throw new RuntimeException("El curso ha alcanzado su capacidad máxima");
    }
    
    Inscripcion inscripcion = new Inscripcion();
    inscripcion.setUsuario(usuario);
    inscripcion.setCurso(curso);
    inscripcion.setFechaInscripcion(LocalDateTime.now());
    
    return inscripcionRepository.save(inscripcion);
}
```

---

### **4. AuthController.java - Refactorizado**
**Antes:**
```java
// ❌ Inyectando UsuarioRepository y PasswordEncoder
private final UsuarioRepository usuarioRepository;
private final PasswordEncoder passwordEncoder;
```

**Ahora:**
```java
// ✅ Solo inyectando el servicio
private final UsuarioService usuarioService;
```

---

### **5. CursoController.java - Refactorizado**
**Antes:**
```java
// ❌ Inyectando 3 repositorios
private final CursoRepository cursoRepository;
private final UsuarioRepository usuarioRepository;
private final InscripcionRepository inscripcionRepository;
```

**Ahora:**
```java
// ✅ Inyectando 3 servicios
private final CursoService cursoService;
private final UsuarioService usuarioService;
private final InscripcionService inscripcionService;
```

---

## ✅ Beneficios de la Refactorización

1. **Separación de Responsabilidades**: Cada capa tiene una función clara
2. **Código Más Limpio**: Los controladores son más simples y legibles
3. **Reutilización**: Los métodos de los servicios pueden usarse en múltiples controladores
4. **Testabilidad**: Es más fácil hacer pruebas unitarias de los servicios
5. **Mantenibilidad**: Cambios en la lógica de negocio solo afectan a los servicios
6. **Validaciones Centralizadas**: Todas las validaciones están en los servicios
7. **Transacciones**: Los métodos críticos están marcados con `@Transactional`

---

## 📝 Convenciones Utilizadas

- **Nomenclatura clara**: Métodos con nombres descriptivos
- **Javadoc**: Comentarios explicativos en cada método
- **Manejo de excepciones**: Uso de `Optional` y excepciones con mensajes claros
- **Inyección de dependencias**: Constructor injection (mejor práctica)
- **Anotaciones apropiadas**: `@Service`, `@Transactional`, etc.

---

## 🚀 Próximas Mejoras Recomendadas

1. **DTOs (Data Transfer Objects)**: Para evitar exponer entidades directamente
2. **Excepciones personalizadas**: En lugar de `RuntimeException`
3. **Mensajes de error**: Usando flash attributes para mostrar al usuario
4. **Logging**: Agregar logs para seguimiento de operaciones
5. **Validaciones más robustas**: Usar Bean Validation en los servicios

---

## 📚 Flujo de Datos Actual

```
Usuario (HTTP Request)
        ↓
   Controller (Valida entrada, delega)
        ↓
    Service (Lógica de negocio, validaciones)
        ↓
   Repository (Acceso a BD)
        ↓
   Base de Datos
```

---

**Autor**: Refactorización aplicada según mejores prácticas de Spring Boot
**Fecha**: Octubre 2025
