package com.minimarket.minimarket_segu.servicios;

import com.minimarket.minimarket_segu.modelo.Producto;

/** Puerto de persistencia utilizado al actualizar existencias. */
public interface InventarioRepositorio {
    void actualizar(Producto producto);
}
