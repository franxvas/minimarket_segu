package com.minimarket.minimarket_segu.servicios;

import com.minimarket.minimarket_segu.modelo.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Aplica de forma atomica las entradas y salidas de inventario.
 * La transaccion la proporciona la accion OpenXava que invoca el servicio.
 */
public class InventarioServicio {

    InventarioRepositorio repositorio;

    public InventarioServicio(InventarioRepositorio repositorio) {
        if (repositorio == null) throw new IllegalArgumentException("repositorio es obligatorio");
        this.repositorio = repositorio;
    }

    public void procesarVenta(Venta venta) {
        if (venta == null) throw new ReglaNegocioException("venta_no_encontrada");
        if (venta.isProcesada()) throw new ReglaNegocioException("venta_ya_procesada");
        validarDetallesVenta(venta.getDetalles());

        Map<Producto, Integer> cantidades = agruparCantidadesVenta(venta.getDetalles());
        for (Map.Entry<Producto, Integer> linea : cantidades.entrySet()) {
            Producto producto = linea.getKey();
            int cantidad = linea.getValue();
            if (producto.getStock() < cantidad) {
                throw new ReglaNegocioException("stock_insuficiente");
            }
        }
        cantidades.forEach((producto, cantidad) -> {
            producto.setStock(producto.getStock() - cantidad);
            repositorio.actualizar(producto);
        });
        venta.setProcesada(true);
    }

    public void procesarCompra(Compra compra) {
        if (compra == null) throw new ReglaNegocioException("compra_no_encontrada");
        if (compra.isProcesada()) throw new ReglaNegocioException("compra_ya_procesada");
        validarDetallesCompra(compra.getDetalles());

        for (DetalleCompra detalle : compra.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(Math.addExact(producto.getStock(), detalle.getCantidad()));
            repositorio.actualizar(producto);
        }
        compra.setProcesada(true);
    }

    private Map<Producto, Integer> agruparCantidadesVenta(Collection<DetalleVenta> detalles) {
        Map<Producto, Integer> cantidades = new IdentityHashMap<>();
        for (DetalleVenta detalle : detalles) {
            cantidades.merge(detalle.getProducto(), detalle.getCantidad(), Math::addExact);
        }
        return cantidades;
    }

    private void validarDetallesVenta(Collection<DetalleVenta> detalles) {
        if (detalles == null || detalles.isEmpty()) throw new ReglaNegocioException("venta_sin_detalles");
        for (DetalleVenta detalle : detalles) {
            if (detalle == null || detalle.getProducto() == null) throw new ReglaNegocioException("producto_requerido");
            if (detalle.getCantidad() <= 0) throw new ReglaNegocioException("cantidad_invalida");
            BigDecimal precio = detalle.getPrecio();
            if (precio == null || precio.signum() <= 0) throw new ReglaNegocioException("precio_invalido");
        }
    }

    private void validarDetallesCompra(Collection<DetalleCompra> detalles) {
        if (detalles == null || detalles.isEmpty()) throw new ReglaNegocioException("compra_sin_detalles");
        for (DetalleCompra detalle : detalles) {
            if (detalle == null || detalle.getProducto() == null) throw new ReglaNegocioException("producto_requerido");
            if (detalle.getCantidad() <= 0) throw new ReglaNegocioException("cantidad_invalida");
            BigDecimal precio = detalle.getPrecio();
            if (precio == null || precio.signum() <= 0) throw new ReglaNegocioException("precio_invalido");
        }
    }
}
