package com.minimarket.minimarket_segu.pruebas;

import org.openxava.tests.ModuleTestBase;

/** Prueba funcional CRUD representativa sobre un modulo OpenXava. */
public class CategoriaProductoCrudTest extends ModuleTestBase {

    public CategoriaProductoCrudTest(String testName) {
        super(testName, "minimarket_segu", "CategoriaProducto");
    }

    public void testCrearLeerActualizarEliminar() throws Exception {
        login("admin", "admin");
        String nombrePrueba = "Categoria prueba " + System.currentTimeMillis();
        execute("CRUD.new");
        setValue("nombre", nombrePrueba);
        setValue("descripcion", "Registro temporal de la prueba funcional");
        setValue("estado", "true");
        execute("CRUD.save");
        assertNoErrors();

        execute("Mode.list");
        setConditionValues(nombrePrueba);
        execute("List.filter");
        assertListRowCount(1);
        execute("List.viewDetail", "row=0");
        assertValue("nombre", nombrePrueba);
        setValue("descripcion", "Registro temporal actualizado");
        execute("CRUD.save");
        assertNoErrors();

        execute("Mode.list");
        setConditionValues(nombrePrueba);
        execute("List.filter");
        assertListRowCount(1);
        execute("List.viewDetail", "row=0");
        assertValue("descripcion", "Registro temporal actualizado");
        execute("CRUD.delete");
        assertNoErrors();
    }
}
