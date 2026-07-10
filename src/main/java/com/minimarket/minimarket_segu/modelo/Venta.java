package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

/**
 * Entidad que representa una venta realizada en el minimarket.
 * Incluye datos del comprobante, cliente, empleado, metodo de pago y detalles.
 */
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(name = "uk_venta_comprobante", columnNames = {"tipoComprobante", "numeroComprobante"}),
    indexes = {@Index(name = "idx_venta_fecha", columnList = "fecha"), @Index(name = "idx_venta_cliente", columnList = "cliente_id")}
)
@Getter @Setter
@Tab(properties = "fecha, cliente.nombre, empleado.nombre, metodoPago.nombre, tipoComprobante, numeroComprobante, procesada, total")
@View(members =
    "DatosComprobante [" +
        "fecha, tipoComprobante, numeroComprobante;" +
        "cliente, empleado, metodoPago" +
    "];" +
    "detalles;" +
    "total"
)
public class Venta {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class)
    Date fecha;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @Required
    @DescriptionsList
    Cliente cliente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    @Required
    @DescriptionsList
    Empleado empleado;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "metodoPago_id", nullable = false)
    @Required
    @DescriptionsList
    MetodoPago metodoPago;

    @Required
    @Enumerated(EnumType.STRING)
    @Column(length = 12, nullable = false)
    TipoComprobante tipoComprobante;

    public enum TipoComprobante {
        BOLETA, FACTURA, TICKET
    }

    @Required
    @Column(length = 20, nullable = false)
    String numeroComprobante;

    @ElementCollection
    @CollectionTable(name = "detalle_venta", joinColumns = @JoinColumn(name = "venta_id"))
    @OrderColumn(name = "linea")
    @ListProperties("producto.nombre, cantidad, precio, descuentoVolumen, subtotal")
    List<DetalleVenta> detalles = new ArrayList<>();

    @ReadOnly
    boolean procesada;

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
    }

    @AssertTrue(message = "La venta debe contener al menos un detalle")
    public boolean isConDetalles() {
        return detalles != null && !detalles.isEmpty();
    }

    @AssertTrue(message = "Para emitir una FACTURA, el cliente debe tener un RUC registrado")
    public boolean isRucValidoParaFactura() {
        if (tipoComprobante == TipoComprobante.FACTURA) {
            if (cliente == null) return false;
            if (cliente.getRuc() == null) return false;
            if (cliente.getRuc().trim().isEmpty()) return false;
        }
        return true;
    }

    @Depends("detalles.subtotal")
    @ReadOnly
    @Money
    public BigDecimal getTotal() {
        if (detalles == null) return BigDecimal.ZERO;
        return detalles.stream()
            .map(DetalleVenta::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
