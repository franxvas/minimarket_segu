package com.minimarket.minimarket_segu.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DetalleVentaTest {

    @Test
    void getPrecioUsesMayoristaPriceForMayoristaClients() {
        DetalleVenta detalle = new DetalleVenta();
        Producto producto = new Producto();
        producto.setPrecioMayorista(80.0);
        producto.setPrecioVenta(100.0);

        Venta venta = new Venta();
        Cliente cliente = new Cliente();
        cliente.setTipoCliente(Cliente.TipoCliente.MAYORISTA);
        venta.setCliente(cliente);

        detalle.setProducto(producto);
        detalle.setVenta(venta);

        assertEquals(80.0, detalle.getPrecio());
    }

    @Test
    void getDescuentoVolumenAppliesFivePercentWhenCantidadIsAtLeastTwelve() {
        DetalleVenta detalle = new DetalleVenta();
        Producto producto = new Producto();
        producto.setPrecioVenta(10.0);

        Venta venta = new Venta();
        detalle.setProducto(producto);
        detalle.setVenta(venta);
        detalle.setCantidad(12);

        assertEquals(6.0, detalle.getDescuentoVolumen());
    }

    @Test
    void getSubtotalAccountsForQuantityPriceAndDiscount() {
        DetalleVenta detalle = new DetalleVenta();
        Producto producto = new Producto();
        producto.setPrecioVenta(10.0);

        detalle.setProducto(producto);
        detalle.setCantidad(12);

        assertEquals(114.0, detalle.getSubtotal());
    }

    @Test
    void isCantidadValidaRejectsZeroOrNegativeValues() {
        DetalleVenta detalle = new DetalleVenta();

        detalle.setCantidad(0);
        assertFalse(detalle.isCantidadValida());

        detalle.setCantidad(-1);
        assertFalse(detalle.isCantidadValida());

        detalle.setCantidad(3);
        assertTrue(detalle.isCantidadValida());
    }

    @Test
    void isStockSuficienteReturnsTrueWhenNoProductOrEnoughStock() {
        DetalleVenta detalle = new DetalleVenta();
        assertTrue(detalle.isStockSuficiente());

        Producto producto = new Producto();
        producto.setStock(5);
        detalle.setProducto(producto);
        detalle.setCantidad(4);
        assertTrue(detalle.isStockSuficiente());

        detalle.setCantidad(6);
        assertFalse(detalle.isStockSuficiente());
    }
}
