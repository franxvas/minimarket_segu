package com.minimarket.minimarket_segu.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Depends;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Money;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.Required;

@Entity
@Getter @Setter
public class DetalleCompra {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Required
    private Compra compra;

    @ManyToOne
    @Required
    private Producto producto;

    @Required
    private int cantidad;

    @Required
    @Money
    private double precio;
    
    @Depends("cantidad, precio")
    @ReadOnly
    @Money
    public double getSubtotal() {
        return cantidad * precio;
    }
    
    @AssertTrue(message="La cantidad de compra no puede ser negativa o cero")
    public boolean isCantidadValida() {
        return cantidad > 0;
    }
}