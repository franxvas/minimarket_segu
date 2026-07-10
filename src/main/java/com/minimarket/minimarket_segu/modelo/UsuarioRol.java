/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.calculators.CurrentDateCalculator;
/**
 *
 * @author segun
 */
@Entity
@Getter @Setter
public class UsuarioRol {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Required
    private UsuarioSistema usuario;

    @ManyToOne
    @Required
    private Rol rol;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    private Date fechaAsignacion;

    @Required
    private boolean estado;

    @Required
    private String usuarioRegistro;
}
