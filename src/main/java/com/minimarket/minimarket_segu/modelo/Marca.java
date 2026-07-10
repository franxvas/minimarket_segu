package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa una marca de productos.
 */
@Entity
@Getter @Setter
public class Marca {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @Column(length = 40)
    String nombre;

    @Column(length = 40)
    String paisOrigen;

    @Column(length = 150)
    String descripcion;

    @Required
    boolean estado;

    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaRegistro;
}
