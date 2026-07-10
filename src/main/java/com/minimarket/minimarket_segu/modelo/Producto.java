package com.minimarket.minimarket_segu.modelo;

import javax.persistence.Column;
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
public class Producto {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Column(length = 50)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @Required
    @Money
    private double precioCompra;

    @Required
    @Money
    private double precioVenta;

    @Required
    @Money
    private double precioMayorista;

    @Required
    private int stock;

    @ManyToOne
    @Required
    private CategoriaProducto categoria;

    @ManyToOne
    @Required
    private Marca marca;
    
    @AssertTrue(message="El precio de compra no puede ser negativo")
    public boolean isPrecioCompraValido() {
        return precioCompra >= 0;
    }
    
    @AssertTrue(message="El precio de venta no puede ser negativo")
    public boolean isPrecioVentaValido() {
        return precioVenta >= 0;
    }

    @AssertTrue(message="El precio mayorista no puede ser negativo")
    public boolean isPrecioMayoristaValido() {
        return precioMayorista >= 0;
    }

    @Depends("precioCompra, precioVenta")
    @ReadOnly
    @Money
    public double getGanancia() {
        return precioVenta - precioCompra;
    }
}