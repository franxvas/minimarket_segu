/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.minimarket.minimarket_segu.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
/**
 *
 * @author segun
 */
@Entity
@Getter @Setter
public class Cargo {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Column(length = 50)
    private String nombre;

    @Column(length = 150)
    private String descripcion;

    @Required
    private double sueldoBase;

    @Required
    private boolean estado;

    @Column(length = 30)
    private String usuarioRegistro;
}
