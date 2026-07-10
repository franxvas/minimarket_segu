/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.minimarket.minimarket_segu.modelo;

import java.util.Date;
import javax.persistence.Column;
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
public class UsuarioSistema {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Column(length = 30)
    private String username;

    @Required
    @Column(length = 100)
    private String password;

    @ManyToOne
    @Required
    private Empleado empleado;

    @Required
    private boolean estado;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    private Date fechaRegistro;

    private Date ultimoAcceso;
}
