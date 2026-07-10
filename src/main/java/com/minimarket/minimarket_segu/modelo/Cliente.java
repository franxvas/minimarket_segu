package com.minimarket.minimarket_segu.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.Depends;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Money;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.Required;
import org.openxava.calculators.CurrentDateCalculator;

@Entity
@Getter @Setter
public class Cliente {
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

    @Column(length = 11)
    private String ruc;

    @Column(length = 100)
    private String razonSocial;

    @Required
    @DefaultValueCalculator(value = org.openxava.calculators.EnumCalculator.class, 
        properties = @org.openxava.annotations.PropertyValue(name = "value", value = "REGULAR"))
    private TipoCliente tipoCliente;

    public enum TipoCliente {
        REGULAR, MAYORISTA
    }

    @Column(length = 120)
    private String direccion;

    @Column(length = 15)
    private String telefono;

    @Column(length = 60)
    private String correo;

    @DefaultValueCalculator(CurrentDateCalculator.class)
    private java.util.Date fechaRegistro;

    @Depends("id")
    @ReadOnly
    @Money
    public double getMontoTotalGastado() {
        return 0;
    }
}