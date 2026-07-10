package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa una compra realizada a un proveedor.
 * Incluye datos del comprobante, proveedor y detalles de compra.
 */
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(name = "uk_compra_comprobante", columnNames = {"tipoComprobante", "numeroComprobante"}),
    indexes = {@Index(name = "idx_compra_fecha", columnList = "fecha"), @Index(name = "idx_compra_proveedor", columnList = "proveedor_id")}
)
@Getter @Setter
@Tab(properties = "fecha, proveedor.nombre, tipoComprobante, numeroComprobante, procesada, total")
@View(members =
    "DatosCompra [" +
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    @Required
    @DescriptionsList
    Proveedor proveedor;

    @Required
    @Column(length = 20, nullable = false)
    String tipoComprobante;

    @Required
    @Column(length = 20, nullable = false)
    String numeroComprobante;

    @Required
    boolean estado;

    @Column(length = 30)
    @ReadOnly
    String usuarioRegistro;

    @ElementCollection
    @CollectionTable(name = "detalle_compra", joinColumns = @JoinColumn(name = "compra_id"))
    @OrderColumn(name = "linea")
    @ListProperties("producto.nombre, cantidad, precio, subtotal")
    List<DetalleCompra> detalles = new ArrayList<>();

    @ReadOnly
    boolean procesada;

    public void agregarDetalle(DetalleCompra detalle) {
        detalles.add(detalle);
    }

    @AssertTrue(message = "La compra debe contener al menos un detalle")
    public boolean isConDetalles() {
        return detalles != null && !detalles.isEmpty();
    }

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
