package com.minimarket.minimarket_segu.pruebas;

import com.minimarket.minimarket_segu.modelo.*;
import com.minimarket.minimarket_segu.servicios.*;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.mockito.ArgumentCaptor;
import org.openxava.tests.ModuleTestBase;
import static org.mockito.Mockito.*;

/** Pruebas Mockito de las reglas de compra y venta. */
public class InventarioServicioTest extends ModuleTestBase {

    public InventarioServicioTest(String testName) {
        super(testName, "minimarket_segu", "Producto");
    }

    public void testVentaDescuentaStockYPersiste() throws Exception {
        login("admin", "admin");
        InventarioRepositorio repositorio = mock(InventarioRepositorio.class);
        Producto producto = productoConStock(10);
        Venta venta = venta(producto, 4);
        new InventarioServicio(repositorio).procesarVenta(venta);
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(repositorio).actualizar(captor.capture());
        assertSame(producto, captor.getValue());
        assertEquals(6, producto.getStock());
        assertTrue(venta.isProcesada());
    }

    public void testVentaSinStockNoPersisteCambios() throws Exception {
        login("admin", "admin");
        InventarioRepositorio repositorio = mock(InventarioRepositorio.class);
        Producto producto = productoConStock(2);
        Venta venta = venta(producto, 3);
        try {
            new InventarioServicio(repositorio).procesarVenta(venta);
            fail("Debio rechazar la venta sin stock");
        }
        catch (ReglaNegocioException ex) {
            assertEquals("stock_insuficiente", ex.getMessage());
        }
        verifyNoInteractions(repositorio);
        assertEquals(2, producto.getStock());
    }

    public void testCompraIncrementaStockYNoSeProcesaDosVeces() throws Exception {
        login("admin", "admin");
        InventarioRepositorio repositorio = mock(InventarioRepositorio.class);
        Producto producto = productoConStock(5);
        Compra compra = compra(producto, 7);
        InventarioServicio servicio = new InventarioServicio(repositorio);
        servicio.procesarCompra(compra);
        assertEquals(12, producto.getStock());
        verify(repositorio).actualizar(producto);
        try {
            servicio.procesarCompra(compra);
            fail("Debio impedir el reproceso");
        }
        catch (ReglaNegocioException ex) {
            assertEquals("compra_ya_procesada", ex.getMessage());
        }
        verifyNoMoreInteractions(repositorio);
    }

    public void testJpaRepositorioFusionaProductoDesacoplado() throws Exception {
        login("admin", "admin");
        EntityManager entityManager = mock(EntityManager.class);
        Producto producto = productoConStock(8);
        when(entityManager.contains(producto)).thenReturn(false);

        new JpaInventarioRepositorio(entityManager).actualizar(producto);

        verify(entityManager).merge(producto);
    }

    public void testErrorDeRepositorioSePropaga() throws Exception {
        login("admin", "admin");
        InventarioRepositorio repositorio = mock(InventarioRepositorio.class);
        Producto producto = productoConStock(10);
        Venta venta = venta(producto, 1);
        doThrow(new PersistenceException("BD no disponible")).when(repositorio).actualizar(producto);
        try {
            new InventarioServicio(repositorio).procesarVenta(venta);
            fail("Debio propagar el error de persistencia");
        }
        catch (PersistenceException ex) {
            assertEquals("BD no disponible", ex.getMessage());
        }
    }

    private Producto productoConStock(int stock) {
        Producto producto = new Producto();
        producto.setStock(stock);
        return producto;
    }

    private Venta venta(Producto producto, int cantidad) {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecio(new BigDecimal("5.00"));
        Venta venta = new Venta();
        venta.agregarDetalle(detalle);
        return venta;
    }

    private Compra compra(Producto producto, int cantidad) {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecio(new BigDecimal("3.00"));
        Compra compra = new Compra();
        compra.agregarDetalle(detalle);
        return compra;
    }
}
