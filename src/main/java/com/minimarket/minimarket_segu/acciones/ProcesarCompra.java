package com.minimarket.minimarket_segu.acciones;

import com.minimarket.minimarket_segu.modelo.Compra;
import com.minimarket.minimarket_segu.servicios.*;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

/** Confirma una compra guardada e incrementa el inventario. */
public class ProcesarCompra extends ViewBaseAction {

    @Override
    public void execute() {
        Object id = getView().getValue("id");
        if (id == null) {
            addError("compra_debe_guardarse");
            return;
        }
        try {
            Compra compra = XPersistence.getManager().find(Compra.class, id);
            InventarioRepositorio repositorio = new JpaInventarioRepositorio(XPersistence.getManager());
            new InventarioServicio(repositorio).procesarCompra(compra);
            addMessage("compra_procesada");
            getView().refresh();
        }
        catch (ReglaNegocioException ex) {
            addError(ex.getMessage());
        }
    }
}
