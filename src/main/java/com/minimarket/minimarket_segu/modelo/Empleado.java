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
@Getter @Setter
@View(members =
    "Datos Personales [" +
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
    @Column(length = 80)
    String nombre;

    @Required
    @Column(length = 8)
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

    @ManyToOne
    @Required
    @DescriptionsList
    Cargo cargo;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaIngreso;
}
