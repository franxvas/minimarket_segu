package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa un usuario del sistema con credenciales de acceso.
 * Vinculado a un Empleado.
 */
@Entity
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
    @Column(length = 30, unique = true)
    String username;

    @Required
    @Column(length = 100)
    String password;

    @ManyToOne
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
