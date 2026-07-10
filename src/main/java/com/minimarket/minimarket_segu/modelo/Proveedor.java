package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa un proveedor del minimarket.
 * Almacena datos de contacto, RUC y estado activo/inactivo.
 */
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_proveedor_ruc", columnNames = "ruc"),
        @UniqueConstraint(name = "uk_proveedor_correo", columnNames = "correo")
    },
    indexes = @Index(name = "idx_proveedor_nombre", columnList = "nombre")
)
@Getter @Setter
@Tab(properties = "nombre, ruc, telefono, correo, estado")
@View(members =
    "DatosProveedor [" +
        "nombre; ruc;" +
        "direccion" +
    "];" +
    "Contacto [" +
        "telefono, correo" +
    "];" +
    "estado, fechaRegistro"
)
public class Proveedor {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @Column(length = 80, nullable = false)
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    String nombre;

    @Required
    @Column(length = 11, nullable = false)
    @Pattern(regexp = "\\d{11}", message = "El RUC debe tener exactamente 11 digitos numericos")
    String ruc;

    @Column(length = 120)
    String direccion;

    @Column(length = 15)
    @Pattern(regexp = "(\\d{6,15})?", message = "El telefono debe contener entre 6 y 15 digitos")
    String telefono;

    @Column(length = 60)
    @Email(message = "El correo electronico no tiene un formato valido")
    String correo;

    @Required
    boolean estado;

    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaRegistro;
}
