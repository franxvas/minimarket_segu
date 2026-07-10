package com.minimarket.minimarket_segu.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DetalleCompraTest {

    @Test
    void getSubtotalReturnsCantidadTimesPrecio() {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setCantidad(4);
        detalle.setPrecio(12.5);

        assertEquals(50.0, detalle.getSubtotal());
    }

    @Test
    void isCantidadValidaRejectsZeroOrNegativeValues() {
        DetalleCompra detalle = new DetalleCompra();

        detalle.setCantidad(0);
        assertFalse(detalle.isCantidadValida());

        detalle.setCantidad(-3);
        assertFalse(detalle.isCantidadValida());

        detalle.setCantidad(2);
        assertTrue(detalle.isCantidadValida());
    }
}
