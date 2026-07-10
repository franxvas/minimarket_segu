package com.minimarket.minimarket_segu.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductoTest {

    @Test
    void isPrecioCompraValidoAcceptsNonNegativeValues() {
        Producto producto = new Producto();

        producto.setPrecioCompra(-1.0);
        assertFalse(producto.isPrecioCompraValido());

        producto.setPrecioCompra(10.5);
        assertTrue(producto.isPrecioCompraValido());
    }

    @Test
    void isPrecioVentaValidoAcceptsNonNegativeValues() {
        Producto producto = new Producto();

        producto.setPrecioVenta(-2.5);
        assertFalse(producto.isPrecioVentaValido());

        producto.setPrecioVenta(15.0);
        assertTrue(producto.isPrecioVentaValido());
    }

    @Test
    void isPrecioMayoristaValidoAcceptsNonNegativeValues() {
        Producto producto = new Producto();

        producto.setPrecioMayorista(-3.0);
        assertFalse(producto.isPrecioMayoristaValido());

        producto.setPrecioMayorista(12.0);
        assertTrue(producto.isPrecioMayoristaValido());
    }

    @Test
    void getGananciaReturnsDifferenceBetweenVentaAndCompra() {
        Producto producto = new Producto();
        producto.setPrecioCompra(20.0);
        producto.setPrecioVenta(35.0);

        assertEquals(15.0, producto.getGanancia());
    }
}
