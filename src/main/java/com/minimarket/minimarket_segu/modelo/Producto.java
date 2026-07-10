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
@Table(
    uniqueConstraints = @UniqueConstraint(name = "uk_producto_nombre", columnNames = "nombre"),
    indexes = {@Index(name = "idx_producto_categoria", columnList = "categoria_id"), @Index(name = "idx_producto_marca", columnList = "marca_id"), @Index(name = "idx_producto_stock", columnList = "stock")}
)
@Getter @Setter
@Tab(properties = "nombre, precioVenta, precioMayorista, stock, stockMinimo, categoria.nombre, marca.nombre, ganancia, bajoStock")
@View(members =
    "InformacionGeneral [" +
        "nombre; descripcion" +
    "];" +
    "PreciosStock [" +
        "precioCompra, precioVenta, precioMayorista;" +
        "stock, stockMinimo; ganancia, bajoStock" +
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
    @Column(length = 50, nullable = false)
    @NotBlank(message = "El nombre del producto es obligatorio")
    String nombre;

    @Column(length = 200)
    String descripcion;

    @Required
    @Money
    @DecimalMin(value = "0.00", message = "El precio de compra no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal precioCompra;

    @Required
    @Money
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a cero")
    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal precioVenta;

    @Required
    @Money
    @DecimalMin(value = "0.00", message = "El precio mayorista no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal precioMayorista;

    @Required
    @Min(value = 0, message = "El stock no puede ser negativo")
    int stock;

    @Required
    @Min(value = 0, message = "El stock minimo no puede ser negativo")
    int stockMinimo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @Required
    @DescriptionsList
    CategoriaProducto categoria;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
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

    @Depends("stock, stockMinimo")
    @ReadOnly
    public boolean isBajoStock() {
        return stock <= stockMinimo;
    }
}
