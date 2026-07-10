package com.minimarket.minimarket_segu.pruebas;

import com.minimarket.minimarket_segu.acciones.ProcesarCompra;
import com.minimarket.minimarket_segu.acciones.ProcesarVenta;
import org.openxava.controller.ModuleContext;
import org.openxava.tests.ModuleTestBase;
import org.openxava.util.Messages;
import org.openxava.view.View;
import static org.mockito.Mockito.*;

/** Pruebas unitarias de las acciones cuando el documento aun no fue guardado. */
public class AccionesTest extends ModuleTestBase {

    public AccionesTest(String testName) {
        super(testName, "minimarket_segu", "Venta");
    }

    public void testVentaDebeGuardarseAntesDeProcesar() throws Exception {
        login("admin", "admin");
        View view = mock(View.class);
        when(view.getValue("id")).thenReturn(null);
        ProcesarVenta accion = new ProcesarVenta();
        accion.setContext(mock(ModuleContext.class));
        accion.setErrors(new Messages());
        accion.setView(view);
        accion.execute();
        assertTrue(accion.getErrors().contains("venta_debe_guardarse"));
    }

    public void testCompraDebeGuardarseAntesDeProcesar() throws Exception {
        login("admin", "admin");
        View view = mock(View.class);
        when(view.getValue("id")).thenReturn(null);
        ProcesarCompra accion = new ProcesarCompra();
        accion.setContext(mock(ModuleContext.class));
        accion.setErrors(new Messages());
        accion.setView(view);
        accion.execute();
        assertTrue(accion.getErrors().contains("compra_debe_guardarse"));
    }
}
