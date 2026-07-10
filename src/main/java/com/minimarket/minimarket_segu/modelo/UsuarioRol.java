package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que asocia un usuario del sistema con un rol.
 */
@Entity
@Getter @Setter
public class UsuarioRol {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @Required
    UsuarioSistema usuario;

    @ManyToOne
    @Required
    @DescriptionsList
    Rol rol;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    Date fechaAsignacion;

    @Required
    boolean estado;

    @Column(length = 30)
    @ReadOnly
    String usuarioRegistro;
}
