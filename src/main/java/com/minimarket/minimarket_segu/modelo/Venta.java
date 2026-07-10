package com.minimarket.minimarket_segu.modelo;

import java.math.BigDecimal;
import java.util.Collection;
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
@Getter @Setter
@View(members =
    "Datos del Comprobante [" +
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

    @ManyToOne
    @Required
    @DescriptionsList
    Cliente cliente;

    @ManyToOne
    @Required
    @DescriptionsList
    Empleado empleado;

    @ManyToOne
    @Required
    @DescriptionsList
    MetodoPago metodoPago;

    @Required
    TipoComprobante tipoComprobante;

    public enum TipoComprobante {
        BOLETA, FACTURA, TICKET
    }

    @Required
    @Column(length = 20)
    String numeroComprobante;

    @ElementCollection
    @ListProperties("producto.nombre, cantidad, precio, descuentoVolumen, subtotal")
    Collection<DetalleVenta> detalles;

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