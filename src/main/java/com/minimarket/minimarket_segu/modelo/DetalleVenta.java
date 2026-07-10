package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;

/**
 * Clase embebida que representa un detalle de venta.
 * Almacena producto, cantidad, precio unitario y calcula subtotales.
 * El precio se congela al momento de registrar la venta.
 */
@Embeddable
@Getter @Setter
public class DetalleVenta {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @Required
    @DescriptionsList
    Producto producto;

    @Required
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    int cantidad;

    @Required
    @Money
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal precio;

    @Depends("cantidad, precio")
    @ReadOnly
    @Money
    public BigDecimal getDescuentoVolumen() {
        if (precio == null) return BigDecimal.ZERO;
        if (cantidad >= 12) {
            BigDecimal bruto = precio.multiply(BigDecimal.valueOf(cantidad));
            return bruto.multiply(new BigDecimal("0.05"));
        }
        return BigDecimal.ZERO;
    }

    @Depends("cantidad, precio, descuentoVolumen")
    @ReadOnly
    @Money
    public BigDecimal getSubtotal() {
        if (precio == null) return BigDecimal.ZERO;
        BigDecimal bruto = precio.multiply(BigDecimal.valueOf(cantidad));
        return bruto.subtract(getDescuentoVolumen());
    }
}
