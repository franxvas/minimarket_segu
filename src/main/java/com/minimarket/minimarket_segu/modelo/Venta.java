package com.minimarket.minimarket_segu.modelo;

import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

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
    private Long id;

    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class)
    private Date fecha;

    @ManyToOne
    @Required
    private Cliente cliente;

    @ManyToOne
    @Required
    private Empleado empleado;

    @ManyToOne
    @Required
    private MetodoPago metodoPago;

    @Required
    private TipoComprobante tipoComprobante;

    public enum TipoComprobante {
        BOLETA, FACTURA, TICKET
    }

    @Required
    @Column(length = 20)
    private String numeroComprobante;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @ListProperties("producto.nombre, cantidad, precio, descuentoVolumen, subtotal")
    private Collection<DetalleVenta> detalles;

    @AssertTrue(message = "Para emitir una FACTURA, el cliente debe tener un RUC registrado en su ficha")
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
    public double getTotal() {
        if (detalles == null) return 0;
        return detalles.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
    }
}