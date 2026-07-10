package com.minimarket.minimarket_segu.modelo;

import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.Depends;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.Money;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.Required;
import org.openxava.calculators.CurrentDateCalculator;

@Entity
@Getter @Setter
public class Compra {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class)
    private Date fecha;

    @ManyToOne
    @Required
    private Proveedor proveedor;

    @Required
    @Column(length = 20)
    private String tipoComprobante;

    @Required
    @Column(length = 20)
    private String numeroComprobante;

    @Required
    private boolean estado;

    @Column(length = 30)
    private String usuarioRegistro;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    @ListProperties("producto.nombre, cantidad, precio, subtotal")
    private Collection<DetalleCompra> detalles;

    @Depends("detalles.subtotal")
    @ReadOnly
    @Money
    public double getTotal() {
        if (detalles == null) return 0;
        return detalles.stream().mapToDouble(DetalleCompra::getSubtotal).sum();
    }
}