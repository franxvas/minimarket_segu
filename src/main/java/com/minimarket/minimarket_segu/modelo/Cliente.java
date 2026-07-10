package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa un cliente del minimarket.
 * Puede ser REGULAR o MAYORISTA, lo que afecta los precios de venta.
 */
@Entity
@Getter @Setter
@View(members =
    "Datos Personales [" +
        "nombre; dni, tipoCliente;" +
        "ruc, razonSocial" +
    "];" +
    "Contacto [" +
        "direccion;" +
        "telefono, correo" +
    "];" +
    "fechaRegistro"
)
public class Cliente {

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

    @Column(length = 11)
    @Pattern(regexp = "(\\d{11})?", message = "El RUC debe tener exactamente 11 dígitos numéricos")
    String ruc;

    @Column(length = 100)
    String razonSocial;

    @Required
    @DefaultValueCalculator(value = org.openxava.calculators.EnumCalculator.class,
        properties = @PropertyValue(name = "value", value = "REGULAR"))
    TipoCliente tipoCliente;

    public enum TipoCliente {
        REGULAR, MAYORISTA
    }

    @Column(length = 120)
    String direccion;

    @Column(length = 15)
    @Pattern(regexp = "(\\d{6,15})?", message = "El teléfono debe contener entre 6 y 15 dígitos")
    String telefono;

    @Column(length = 60)
    @Email(message = "El correo electrónico no tiene un formato válido")
    String correo;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaRegistro;
}