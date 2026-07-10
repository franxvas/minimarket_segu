package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa un rol de usuario en el sistema.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombre"))
@Getter @Setter
public class Rol {

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

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaRegistro;

    @Column(length = 30)
    @ReadOnly
    String usuarioRegistro;
}
