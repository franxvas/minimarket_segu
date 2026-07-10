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
public class Empleado {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Column(length = 80)
    private String nombre;

    @Required
    @Column(length = 8)
    private String dni;

    @Column(length = 120)
    private String direccion;

    @Column(length = 15)
    private String telefono;

    @Column(length = 60)
    private String correo;

    @ManyToOne
    @Required
    private Cargo cargo;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    private Date fechaIngreso;
}
