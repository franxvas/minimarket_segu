package com.minimarket.minimarket_segu.pruebas;

import com.minimarket.minimarket_segu.modelo.*;
import java.math.BigDecimal;
import java.util.Date;
import org.openxava.tests.ModuleTestBase;

/** Pruebas de construccion, relaciones, calculos y reglas de todas las entidades. */
public class EntidadesTest extends ModuleTestBase {

    public EntidadesTest(String testName) {
        super(testName, "minimarket_segu", "Producto");
    }

    public void testCargo() throws Exception {
        login("admin", "admin");
        Cargo cargo = new Cargo();
        cargo.setNombre("Cajero");
        cargo.setSueldoBase(new BigDecimal("1300.00"));
        assertEquals("Cajero", cargo.getNombre());
        assertEquals(new BigDecimal("1300.00"), cargo.getSueldoBase());
    }

    public void testCategoriaProducto() throws Exception {
        login("admin", "admin");
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre("Abarrotes");
        categoria.setFechaRegistro(new Date());
        assertNotNull(categoria.getFechaRegistro());
    }

    public void testCliente() throws Exception {
        login("admin", "admin");
        Cliente cliente = new Cliente();
        cliente.setDni("71234567");
        cliente.setTipoCliente(Cliente.TipoCliente.MAYORISTA);
        assertEquals(Cliente.TipoCliente.MAYORISTA, cliente.getTipoCliente());
    }

    public void testCompraYDetalle() throws Exception {
        login("admin", "admin");
        DetalleCompra detalle = new DetalleCompra();
        detalle.setCantidad(4);
        detalle.setPrecio(new BigDecimal("12.50"));
        Compra compra = new Compra();
        compra.agregarDetalle(detalle);
        assertTrue(compra.isConDetalles());
        assertEquals(new BigDecimal("50.00"), compra.getTotal());
    }

    public void testEmpleado() throws Exception {
        login("admin", "admin");
        Empleado empleado = new Empleado();
        Cargo cargo = new Cargo();
        empleado.setCargo(cargo);
        empleado.setDni("81234567");
        assertSame(cargo, empleado.getCargo());
    }

    public void testMarca() throws Exception {
        login("admin", "admin");
        Marca marca = new Marca();
        marca.setNombre("Marca demo");
        marca.setEstado(true);
        assertTrue(marca.isEstado());
    }

    public void testMetodoPago() throws Exception {
        login("admin", "admin");
        MetodoPago metodo = new MetodoPago();
        metodo.setNombre("Efectivo");
        metodo.setEstado(true);
        assertEquals("Efectivo", metodo.getNombre());
    }

    public void testProducto() throws Exception {
        login("admin", "admin");
        Producto producto = new Producto();
        producto.setPrecioCompra(new BigDecimal("20.00"));
        producto.setPrecioVenta(new BigDecimal("35.00"));
        producto.setStock(4);
        producto.setStockMinimo(5);
        assertEquals(new BigDecimal("15.00"), producto.getGanancia());
        assertTrue(producto.isPrecioVentaMayorQueCompra());
        assertTrue(producto.isBajoStock());
    }

    public void testProveedor() throws Exception {
        login("admin", "admin");
        Proveedor proveedor = new Proveedor();
        proveedor.setRuc("20123456789");
        proveedor.setCorreo("proveedor@demo.pe");
        assertEquals(11, proveedor.getRuc().length());
    }

    public void testRol() throws Exception {
        login("admin", "admin");
        Rol rol = new Rol();
        rol.setNombre("Administrador");
        rol.setEstado(true);
        assertTrue(rol.isEstado());
    }

    public void testUsuarioSistema() throws Exception {
        login("admin", "admin");
        UsuarioSistema usuario = new UsuarioSistema();
        Empleado empleado = new Empleado();
        usuario.setUsername("usuario.demo");
        usuario.setPassword("Demo-1234");
        usuario.setEmpleado(empleado);
        assertSame(empleado, usuario.getEmpleado());
    }

    public void testUsuarioRol() throws Exception {
        login("admin", "admin");
        UsuarioRol asignacion = new UsuarioRol();
        asignacion.setUsuario(new UsuarioSistema());
        asignacion.setRol(new Rol());
        assertNotNull(asignacion.getUsuario());
        assertNotNull(asignacion.getRol());
    }

    public void testVentaYDetalle() throws Exception {
        login("admin", "admin");
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(12);
        detalle.setPrecio(new BigDecimal("10.00"));
        Venta venta = new Venta();
        venta.agregarDetalle(detalle);
        assertEquals(new BigDecimal("6.0000"), detalle.getDescuentoVolumen());
        assertEquals(new BigDecimal("114.0000"), venta.getTotal());
        assertTrue(venta.isConDetalles());
    }
}
