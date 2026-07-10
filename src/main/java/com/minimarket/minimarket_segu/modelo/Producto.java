package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;

/**
 * Entidad que representa un producto del minimarket.
 * Incluye precios de compra, venta y mayorista, stock y relaciones con categoría y marca.
 */
@Entity
@Getter @Setter
@View(members =
    "Información General [" +
        "nombre; descripcion" +
    "];" +
    "Precios y Stock [" +
        "precioCompra, precioVenta, precioMayorista;" +
        "stock; ganancia" +
    "];" +
    "Clasificación [" +
        "categoria; marca" +
    "]"
)
public class Producto {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @Column(length = 50)
    String nombre;

    @Column(length = 200)
    String descripcion;

    @Required
    @Money
    @DecimalMin(value = "0.00", message = "El precio de compra no puede ser negativo")
    BigDecimal precioCompra;

    @Required
    @Money
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a cero")
    BigDecimal precioVenta;

    @Required
    @Money
    @DecimalMin(value = "0.00", message = "El precio mayorista no puede ser negativo")
    BigDecimal precioMayorista;

    @Required
    @Min(value = 0, message = "El stock no puede ser negativo")
    int stock;

    @ManyToOne
    @Required
    @DescriptionsList
    CategoriaProducto categoria;

    @ManyToOne
    @Required
    @DescriptionsList
    Marca marca;

    @Depends("precioCompra, precioVenta")
    @ReadOnly
    @Money
    public BigDecimal getGanancia() {
        if (precioVenta == null || precioCompra == null) return BigDecimal.ZERO;
        return precioVenta.subtract(precioCompra);
    }

    @AssertTrue(message = "El precio de venta debe ser mayor o igual al precio de compra")
    public boolean isPrecioVentaMayorQueCompra() {
        if (precioVenta == null || precioCompra == null) return true;
        return precioVenta.compareTo(precioCompra) >= 0;
    }
}