package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa una categoria de productos del minimarket.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_categoria_nombre", columnNames = "nombre"))
@Getter @Setter
public class CategoriaProducto {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @Column(length = 40, nullable = false)
    String nombre;

    @Column(length = 150)
    String descripcion;

    @Required
    boolean estado;

    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaRegistro;

    @Column(length = 30)
    @ReadOnly
    String usuarioRegistro;
}
