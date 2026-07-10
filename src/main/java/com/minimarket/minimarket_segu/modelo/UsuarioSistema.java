package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa un usuario del sistema con credenciales de acceso.
 * Vinculado a un Empleado.
 */
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(name = "uk_usuario_username", columnNames = "username"),
    indexes = @Index(name = "idx_usuario_empleado", columnList = "empleado_id")
)
@Getter @Setter
@View(members =
    "Credenciales [" +
        "username; password" +
    "];" +
    "empleado;" +
    "estado, fechaRegistro, ultimoAcceso"
)
public class UsuarioSistema {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @Column(length = 30, nullable = false)
    @Pattern(regexp = "[A-Za-z0-9._-]{4,30}", message = "El usuario debe tener entre 4 y 30 caracteres validos")
    String username;

    @Required
    @Column(length = 100, nullable = false)
    @Size(min = 8, max = 100, message = "La contrasena debe tener al menos 8 caracteres")
    String password;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    @Required
    @DescriptionsList
    Empleado empleado;

    @Required
    boolean estado;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaRegistro;

    @ReadOnly
    Date ultimoAcceso;
}
