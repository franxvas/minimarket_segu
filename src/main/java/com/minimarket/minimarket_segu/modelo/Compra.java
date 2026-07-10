package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa una compra realizada a un proveedor.
 * Incluye datos del comprobante, proveedor y detalles de compra.
 */
@Entity
@Getter @Setter
@View(members =
    "Datos de la Compra [" +
        "fecha, tipoComprobante, numeroComprobante;" +
        "proveedor" +
    "];" +
    "estado, usuarioRegistro;" +
    "detalles;" +
    "total"
)
public class Compra {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class)
    Date fecha;

    @ManyToOne
    @Required
    @DescriptionsList
    Proveedor proveedor;

    @Required
    @Column(length = 20)
    String tipoComprobante;

    @Required
    @Column(length = 20)
    String numeroComprobante;

    @Required
    boolean estado;

    @Column(length = 30)
    @ReadOnly
    String usuarioRegistro;

    @ElementCollection
    @ListProperties("producto.nombre, cantidad, precio, subtotal")
    Collection<DetalleCompra> detalles;

    @Depends("detalles.subtotal")
    @ReadOnly
    @Money
    public BigDecimal getTotal() {
        if (detalles == null) return BigDecimal.ZERO;
        return detalles.stream()
            .map(DetalleCompra::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}