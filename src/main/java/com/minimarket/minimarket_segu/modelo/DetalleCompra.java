package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;

/**
 * Clase embebida que representa un detalle de compra a proveedor.
 * Almacena producto, cantidad y precio unitario de compra.
 */
@Embeddable
@Getter @Setter
public class DetalleCompra {

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @DescriptionsList
    Producto producto;

    @Required
    @Min(value = 1, message = "La cantidad de compra debe ser mayor a cero")
    int cantidad;

    @Required
    @Money
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a cero")
    BigDecimal precio;

    @Depends("cantidad, precio")
    @ReadOnly
    @Money
    public BigDecimal getSubtotal() {
        if (precio == null) return BigDecimal.ZERO;
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
}