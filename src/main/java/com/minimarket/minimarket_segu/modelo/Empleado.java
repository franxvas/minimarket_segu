package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa un empleado del minimarket.
 * Vinculado a un Cargo y puede tener un UsuarioSistema asociado.
 */
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_empleado_dni", columnNames = "dni"),
        @UniqueConstraint(name = "uk_empleado_correo", columnNames = "correo")
    },
    indexes = @Index(name = "idx_empleado_nombre", columnList = "nombre")
)
@Getter @Setter
@View(members =
    "DatosPersonales [" +
        "nombre; dni;" +
        "direccion" +
    "];" +
    "Contacto [" +
        "telefono, correo" +
    "];" +
    "cargo, fechaIngreso"
)
public class Empleado {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @Column(length = 80, nullable = false)
    @NotBlank(message = "El nombre del empleado es obligatorio")
    String nombre;

    @Required
    @Column(length = 8, nullable = false)
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos")
    String dni;

    @Column(length = 120)
    String direccion;

    @Column(length = 15)
    @Pattern(regexp = "(\\d{6,15})?", message = "El teléfono debe contener entre 6 y 15 dígitos")
    String telefono;

    @Column(length = 60)
    @Email(message = "El correo electrónico no tiene un formato válido")
    String correo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    @Required
    @DescriptionsList
    Cargo cargo;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaIngreso;
}
