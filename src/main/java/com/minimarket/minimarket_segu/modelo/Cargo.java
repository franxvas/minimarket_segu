package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;

/**
 * Entidad que representa un cargo o puesto de trabajo en el minimarket.
 */
@Entity
@Getter @Setter
public class Cargo {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @Column(length = 50)
    String nombre;

    @Column(length = 150)
    String descripcion;

    @Required
    @Money
    @DecimalMin(value = "0.00", message = "El sueldo base no puede ser negativo")
    BigDecimal sueldoBase;

    @Required
    boolean estado;

    @Column(length = 30)
    @ReadOnly
    String usuarioRegistro;
}
