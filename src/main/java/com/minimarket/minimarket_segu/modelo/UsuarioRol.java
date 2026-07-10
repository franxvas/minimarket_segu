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
@Table(
    uniqueConstraints = @UniqueConstraint(name = "uk_usuario_rol", columnNames = {"usuario_id", "rol_id"}),
    indexes = {@Index(name = "idx_usuario_rol_usuario", columnList = "usuario_id"), @Index(name = "idx_usuario_rol_rol", columnList = "rol_id")}
)
@Getter @Setter
public class UsuarioRol {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Required
    @DescriptionsList(descriptionProperties = "username")
    UsuarioSistema usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
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
