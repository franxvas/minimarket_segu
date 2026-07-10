package com.minimarket.minimarket_segu.acciones;

import com.minimarket.minimarket_segu.modelo.Venta;
import com.minimarket.minimarket_segu.servicios.*;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

/** Confirma una venta guardada y descuenta sus productos del inventario. */
public class ProcesarVenta extends ViewBaseAction {

    @Override
    public void execute() {
        Object id = getView().getValue("id");
        if (id == null) {
            addError("venta_debe_guardarse");
            return;
        }
        try {
            Venta venta = XPersistence.getManager().find(Venta.class, id);
            InventarioRepositorio repositorio = new JpaInventarioRepositorio(XPersistence.getManager());
            new InventarioServicio(repositorio).procesarVenta(venta);
            addMessage("venta_procesada");
            getView().refresh();
        }
        catch (ReglaNegocioException ex) {
            addError(ex.getMessage());
        }
    }
}
