package com.minimarket.minimarket_segu.servicios;

import com.minimarket.minimarket_segu.modelo.Producto;
import javax.persistence.EntityManager;

/** Adaptador JPA del inventario. */
public class JpaInventarioRepositorio implements InventarioRepositorio {

    EntityManager entityManager;

    public JpaInventarioRepositorio(EntityManager entityManager) {
        if (entityManager == null) throw new IllegalArgumentException("entityManager es obligatorio");
        this.entityManager = entityManager;
    }

    @Override
    public void actualizar(Producto producto) {
        if (!entityManager.contains(producto)) entityManager.merge(producto);
    }
}
