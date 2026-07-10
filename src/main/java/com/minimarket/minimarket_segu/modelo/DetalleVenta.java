package com.minimarket.minimarket_segu.modelo;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;

@Entity
@Getter @Setter
public class DetalleVenta {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Required
    private Venta venta;

    @ManyToOne
    @Required
    private Producto producto;

    @Required
    private int cantidad;

    @Depends("producto, venta.cliente")
    @ReadOnly
    @Money
    public double getPrecio() {
        if (producto == null) return 0;
        
        if (venta != null && venta.getCliente() != null && 
            venta.getCliente().getTipoCliente() == Cliente.TipoCliente.MAYORISTA) {
            return producto.getPrecioMayorista();
        }
        
        return producto.getPrecioVenta();
    }

    @Depends("cantidad, producto")
    @ReadOnly
    @Money
    public double getDescuentoVolumen() {
        if (cantidad >= 12) { 
            return (getPrecio() * cantidad) * 0.05; 
        }
        return 0;
    }

    @Depends("cantidad, precio, descuentoVolumen")
    @ReadOnly
    @Money
    public double getSubtotal() {
        return (cantidad * getPrecio()) - getDescuentoVolumen();
    }

    @AssertTrue(message="La cantidad debe ser mayor a cero")
    public boolean isCantidadValida() {
        return cantidad > 0;
    }

    @AssertTrue(message="No hay suficiente stock disponible en el inventario para este producto")
    public boolean isStockSuficiente() {
        if (producto == null) return true;
        return producto.getStock() >= cantidad;
    }
}