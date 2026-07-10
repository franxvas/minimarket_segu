package com.minimarket.minimarket_segu.datos;

import com.minimarket.minimarket_segu.modelo.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.*;
import java.util.*;
import java.util.function.IntFunction;
import javax.persistence.*;

/**
 * Recarga controlada de datos ficticios y realistas para la demostracion.
 * Nunca se ejecuta durante el arranque normal de OpenXava.
 */
public final class CargaMasivaDatos {

    static final String ARGUMENTO = "--reset-and-seed";
    static final String VARIABLE_CONFIRMACION = "MINIMARKET_SEED_CONFIRM";
    static final String VALOR_CONFIRMACION = "RESET_REALISTIC_DATA";
    static final int LOTE = 100;
    static final long SEMILLA = 20260710L;
    static final int STOCK_INICIAL = 20;

    static final int TOTAL_CARGOS = 8;
    static final int TOTAL_CATEGORIAS = 28;
    static final int TOTAL_MARCAS = 120;
    static final int TOTAL_METODOS = 5;
    static final int TOTAL_ROLES = 6;
    static final int TOTAL_CLIENTES = 3000;
    static final int TOTAL_PROVEEDORES = 80;
    static final int TOTAL_EMPLEADOS = 24;
    static final int TOTAL_PRODUCTOS = 1800;
    static final int TOTAL_USUARIOS = 12;
    static final int TOTAL_COMPRAS = 900;
    static final int TOTAL_VENTAS = 6000;

    static final String[] USUARIOS_AUDITORIA = {
        "admin", "gerente", "supervisor01", "caja01", "caja02", "almacen01", "compras01"
    };

    EntityManagerFactory emf;
    EntityManager em;
    List<ProductoDato> catalogoProductos;
    int[] entradas;
    int[] salidas;

    public static void main(String[] args) {
        validarConfirmacion(args);
        new CargaMasivaDatos().ejecutar();
    }

    static void validarConfirmacion(String[] args) {
        boolean argumentoValido = args != null && Arrays.asList(args).contains(ARGUMENTO);
        boolean entornoValido = VALOR_CONFIRMACION.equals(System.getenv(VARIABLE_CONFIRMACION));
        if (!argumentoValido || !entornoValido) {
            throw new IllegalStateException("Recarga cancelada. Use " + ARGUMENTO + " y defina "
                + VARIABLE_CONFIRMACION + "=" + VALOR_CONFIRMACION);
        }
    }

    public void ejecutar() {
        emf = Persistence.createEntityManagerFactory("carga");
        em = emf.createEntityManager();
        try {
            limpiarDatosAnteriores();
            cargarCatalogos();
            cargarActores();
            cargarProductos();
            cargarSeguridad();
            cargarCompras();
            cargarVentas();
            actualizarStockFinal();
            imprimirConteos();
        }
        finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
            emf.close();
        }
    }

    void limpiarDatosAnteriores() {
        System.out.println("Eliminando la carga artificial anterior...");
        em.getTransaction().begin();
        String[] tablas = {"DETALLE_VENTA", "DETALLE_COMPRA", "USUARIOROL", "VENTA", "COMPRA",
            "USUARIOSISTEMA", "PRODUCTO", "EMPLEADO", "CLIENTE", "PROVEEDOR", "METODOPAGO",
            "ROL", "MARCA", "CATEGORIAPRODUCTO", "CARGO"};
        for (String tabla : tablas) em.createNativeQuery("DELETE FROM " + tabla).executeUpdate();
        em.getTransaction().commit();

        em.getTransaction().begin();
        String[] identidades = {"CARGO", "CATEGORIAPRODUCTO", "CLIENTE", "COMPRA", "EMPLEADO", "MARCA",
            "METODOPAGO", "PRODUCTO", "PROVEEDOR", "ROL", "USUARIOROL", "USUARIOSISTEMA", "VENTA"};
        for (String tabla : identidades) {
            em.createNativeQuery("ALTER TABLE " + tabla + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }
        em.getTransaction().commit();
        em.clear();
        System.out.println("Limpieza finalizada; secuencias reiniciadas.");
    }

    void cargarCatalogos() {
        String[][] cargos = {
            {"Administrador del local", "2600.00"}, {"Supervisor de tienda", "2100.00"},
            {"Cajero", "1450.00"}, {"Almacenero", "1550.00"}, {"Reponedor", "1350.00"},
            {"Personal de atencion", "1400.00"}, {"Encargado de compras", "2200.00"},
            {"Contador", "2400.00"}
        };
        insertar(Cargo.class, cargos.length, i -> {
            Cargo e = new Cargo();
            e.setNombre(cargos[i][0]);
            e.setDescripcion("Responsable de " + cargos[i][0].toLowerCase(Locale.ROOT) + " en el minimarket");
            e.setSueldoBase(new BigDecimal(cargos[i][1]));
            e.setEstado(true);
            e.setUsuarioRegistro(USUARIOS_AUDITORIA[i % USUARIOS_AUDITORIA.length]);
            return e;
        });

        String[] categorias = {"Abarrotes", "Bebidas gaseosas", "Agua", "Jugos", "Bebidas energeticas",
            "Lacteos", "Panaderia", "Galletas", "Snacks", "Chocolates", "Golosinas", "Cereales",
            "Conservas", "Fideos", "Arroz", "Azucar", "Aceite", "Condimentos", "Embutidos",
            "Congelados", "Helados", "Higiene personal", "Limpieza del hogar", "Papel higienico",
            "Cuidado infantil", "Productos para mascotas", "Comida preparada", "Cafe e infusiones"};
        insertar(CategoriaProducto.class, categorias.length, i -> {
            CategoriaProducto e = new CategoriaProducto();
            e.setNombre(categorias[i]);
            e.setDescripcion("Productos de " + categorias[i].toLowerCase(Locale.ROOT));
            e.setEstado(true);
            e.setFechaRegistro(fechaRegistro(new Random(SEMILLA + i)));
            e.setUsuarioRegistro(USUARIOS_AUDITORIA[i % USUARIOS_AUDITORIA.length]);
            return e;
        });

        List<FamiliaProducto> familias = familiasProducto();
        LinkedHashSet<String> marcas = new LinkedHashSet<>();
        familias.forEach(f -> marcas.addAll(Arrays.asList(f.marcas)));
        String[] raices = {"Andes", "Amazonia", "Valle", "Norte", "Sol", "Kantu", "Inti", "Misti", "Bosque", "Costa"};
        String[] complementos = {"Selecto", "Natural", "Dorado", "Fresco", "Vital", "Casa", "Origen", "Real", "Puro", "Delicia", "Premium", "Tradicion"};
        for (String raiz : raices) {
            for (String complemento : complementos) {
                if (marcas.size() >= TOTAL_MARCAS) break;
                marcas.add(raiz + " " + complemento);
            }
        }
        List<String> listaMarcas = new ArrayList<>(marcas).subList(0, TOTAL_MARCAS);
        insertar(Marca.class, listaMarcas.size(), i -> {
            Marca e = new Marca();
            e.setNombre(listaMarcas.get(i));
            e.setPaisOrigen(i % 5 == 0 ? "Importado" : "Peru");
            e.setDescripcion("Marca comercial de consumo masivo");
            e.setEstado(i % 20 != 0);
            e.setFechaRegistro(fechaRegistro(new Random(SEMILLA + 1000L + i)));
            return e;
        });

        String[] metodos = {"Efectivo", "Yape", "Plin", "Tarjeta de debito", "Tarjeta de credito"};
        String[] descripciones = {"Pago en monedas o billetes", "Pago movil mediante Yape", "Pago movil mediante Plin",
            "Pago con tarjeta de debito", "Pago con tarjeta de credito"};
        insertar(MetodoPago.class, metodos.length, i -> {
            MetodoPago e = new MetodoPago();
            e.setNombre(metodos[i]);
            e.setDescripcion(descripciones[i]);
            e.setEstado(true);
            e.setFechaRegistro(fechaRegistro(new Random(SEMILLA + 2000L + i)));
            e.setUsuarioRegistro("admin");
            return e;
        });

        String[][] roles = {{"Administrador", "Acceso completo y configuracion"}, {"Gerente", "Consultas y supervision general"},
            {"Supervisor", "Control operativo de turnos"}, {"Cajero", "Registro de clientes y ventas"},
            {"Almacenero", "Productos, inventario y reposicion"}, {"Compras", "Proveedores y compras"}};
        insertar(Rol.class, roles.length, i -> {
            Rol e = new Rol();
            e.setNombre(roles[i][0]);
            e.setDescripcion(roles[i][1]);
            e.setEstado(true);
            e.setFechaRegistro(fechaRegistro(new Random(SEMILLA + 3000L + i)));
            e.setUsuarioRegistro("admin");
            return e;
        });
    }

    void cargarActores() {
        String[] nombres = nombres();
        String[] apellidosPaternos = apellidosPaternos();
        String[] apellidosMaternos = apellidosMaternos();
        String[] vias = {"Jr. Amazonas", "Av. Circunvalacion", "Calle Comercio", "Jr. Lambayeque", "Av. Mariano Melgar",
            "Jr. Sargento Lores", "Calle Los Cedros", "Av. Heroes del Cenepa", "Jr. San Martin", "Calle Las Palmeras"};
        String[] lugares = {"Bagua, Amazonas", "Bagua Grande, Utcubamba", "Chachapoyas, Amazonas", "Jaen, Cajamarca",
            "Pedro Ruiz Gallo, Bongara", "Nieva, Condorcanqui", "Moyobamba, San Martin", "Rioja, San Martin"};
        Set<String> correos = new HashSet<>();
        Random clientesRandom = new Random(SEMILLA + 4000L);
        insertar(Cliente.class, TOTAL_CLIENTES, i -> {
            Cliente e = new Cliente();
            if (i == 0) {
                e.setNombre("Publico General");
                e.setDni("00000000");
                e.setTipoCliente(Cliente.TipoCliente.REGULAR);
                e.setFechaRegistro(fechaHistorica(clientesRandom));
                return e;
            }
            String nombre = nombres[i % nombres.length];
            String paterno = apellidosPaternos[(i / nombres.length) % apellidosPaternos.length];
            String materno = apellidosMaternos[(i / (nombres.length * apellidosPaternos.length)) % apellidosMaternos.length];
            e.setNombre(nombre + " " + paterno + " " + materno);
            e.setDni(String.format("7%07d", i));
            boolean mayorista = i % 12 == 0;
            e.setTipoCliente(mayorista ? Cliente.TipoCliente.MAYORISTA : Cliente.TipoCliente.REGULAR);
            if (mayorista) {
                e.setRuc(rucFicticio(10000 + i));
                e.setRazonSocial("Bodega " + paterno + " " + materno);
            }
            e.setDireccion(vias[clientesRandom.nextInt(vias.length)] + " " + (100 + clientesRandom.nextInt(1800))
                + ", " + lugares[clientesRandom.nextInt(lugares.length)]);
            e.setTelefono("9" + String.format("%08d", 10000000 + i));
            if (clientesRandom.nextDouble() < 0.65) e.setCorreo(correoCliente(nombre, paterno, materno, i, correos));
            e.setFechaRegistro(fechaHistorica(clientesRandom));
            return e;
        });

        String[] giros = {"Alimentos", "Bebidas", "Abarrotes", "Lacteos", "Limpieza", "Consumo", "Distribuciones", "Importaciones"};
        String[] regiones = {"Amazonas", "Bagua", "Utcubamba", "Cenepa", "Marañon", "Chachapoyas", "Nororiente", "San Martin", "Cajamarca", "Andina"};
        String[] formas = {"S.A.C.", "E.I.R.L.", "S.R.L."};
        Random proveedoresRandom = new Random(SEMILLA + 5000L);
        insertar(Proveedor.class, TOTAL_PROVEEDORES, i -> {
            String giro = giros[i % giros.length];
            String region = regiones[(i / giros.length) % regiones.length];
            Proveedor e = new Proveedor();
            e.setNombre((i % 3 == 0 ? "Comercializadora " : i % 3 == 1 ? "Distribuidora " : "Inversiones ")
                + giro + " " + region + " " + formas[i % formas.length]);
            e.setRuc(rucFicticio(20000 + i));
            e.setDireccion("Av. " + region + " " + (200 + proveedoresRandom.nextInt(1600)) + ", "
                + lugares[proveedoresRandom.nextInt(lugares.length)]);
            e.setTelefono("9" + String.format("%08d", 20000000 + i));
            e.setCorreo("ventas." + normalizar(giro) + "." + normalizar(region) + "@gmail.com");
            e.setEstado(i % 16 != 0);
            e.setFechaRegistro(fechaHistorica(proveedoresRandom));
            return e;
        });

        List<Long> cargos = ids(Cargo.class);
        int[] asignacionCargo = {0, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 7, 7, 3, 4};
        Random empleadosRandom = new Random(SEMILLA + 6000L);
        insertar(Empleado.class, TOTAL_EMPLEADOS, i -> {
            String nombre = nombres[(i * 3 + 2) % nombres.length];
            String paterno = apellidosPaternos[(i * 5 + 1) % apellidosPaternos.length];
            String materno = apellidosMaternos[(i * 7 + 4) % apellidosMaternos.length];
            Empleado e = new Empleado();
            e.setNombre(nombre + " " + paterno + " " + materno);
            e.setDni(String.format("8%07d", i + 1));
            e.setDireccion(vias[i % vias.length] + " " + (250 + i * 13) + ", Bagua, Amazonas");
            e.setTelefono("9" + String.format("%08d", 30000000 + i));
            e.setCorreo(normalizar(nombre.split(" ")[0]) + "." + normalizar(paterno) + "@gmail.com");
            e.setCargo(em.getReference(Cargo.class, cargos.get(asignacionCargo[i])));
            e.setFechaIngreso(fechaHistorica(empleadosRandom));
            return e;
        });
    }

    void cargarProductos() {
        List<FamiliaProducto> familias = familiasProducto();
        Map<String, Long> categorias = idsPorNombre(CategoriaProducto.class);
        Map<String, Long> marcas = idsPorNombre(Marca.class);
        List<ProductoDato> candidatos = new ArrayList<>();
        for (FamiliaProducto familia : familias) {
            for (int b = 0; b < familia.marcas.length; b++) {
                // El catalogo de marcas esta limitado a 120 registros. Solo se
                // generan productos para las marcas que efectivamente quedaron
                // seleccionadas, evitando referencias nulas en la recarga.
                if (!marcas.containsKey(familia.marcas[b])) continue;
                for (int v = 0; v < familia.variedades.length; v++) {
                    for (int p = 0; p < familia.presentaciones.length; p++) {
                        String nombre = familia.tipo + " " + familia.marcas[b] + " " + familia.variedades[v]
                            + " " + familia.presentaciones[p];
                        if (nombre.length() > 50) continue;
                        BigDecimal compra = familia.precioBase.add(BigDecimal.valueOf(p * 0.70 + v * 0.18))
                            .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal venta = compra.multiply(BigDecimal.valueOf(1.28 + (b % 3) * 0.03))
                            .setScale(2, RoundingMode.HALF_UP);
                        BigDecimal mayorista = compra.multiply(new BigDecimal("1.17")).setScale(2, RoundingMode.HALF_UP);
                        candidatos.add(new ProductoDato(nombre,
                            familia.tipo + " " + familia.variedades[v].toLowerCase(Locale.ROOT) + " en presentacion " + familia.presentaciones[p],
                            familia.categoria, familia.marcas[b], compra, venta, mayorista));
                    }
                }
            }
        }
        Collections.shuffle(candidatos, new Random(SEMILLA + 7000L));
        LinkedHashMap<String, ProductoDato> unicos = new LinkedHashMap<>();
        for (ProductoDato dato : candidatos) unicos.putIfAbsent(dato.nombre.toLowerCase(Locale.ROOT), dato);
        if (unicos.size() < TOTAL_PRODUCTOS) throw new IllegalStateException("No hay combinaciones suficientes de productos");
        catalogoProductos = new ArrayList<>(unicos.values()).subList(0, TOTAL_PRODUCTOS);
        insertar(Producto.class, catalogoProductos.size(), i -> {
            ProductoDato dato = catalogoProductos.get(i);
            Producto e = new Producto();
            e.setNombre(dato.nombre);
            e.setDescripcion(dato.descripcion);
            e.setPrecioCompra(dato.precioCompra);
            e.setPrecioVenta(dato.precioVenta);
            e.setPrecioMayorista(dato.precioMayorista);
            e.setStock(STOCK_INICIAL);
            e.setStockMinimo(8 + i % 13);
            e.setCategoria(em.getReference(CategoriaProducto.class, categorias.get(dato.categoria)));
            e.setMarca(em.getReference(Marca.class, marcas.get(dato.marca)));
            return e;
        });
        entradas = new int[TOTAL_PRODUCTOS];
        salidas = new int[TOTAL_PRODUCTOS];
    }

    void cargarSeguridad() {
        List<Long> empleados = ids(Empleado.class);
        String[] usuarios = {"admin", "gerente", "supervisor01", "caja01", "caja02", "caja03", "caja04",
            "almacen01", "almacen02", "compras01", "reponedor01", "reponedor02"};
        insertar(UsuarioSistema.class, usuarios.length, i -> {
            UsuarioSistema e = new UsuarioSistema();
            e.setUsername(usuarios[i]);
            e.setPassword("Minimarket#2026");
            e.setEmpleado(em.getReference(Empleado.class, empleados.get(i)));
            e.setEstado(true);
            e.setFechaRegistro(fechaRegistro(new Random(SEMILLA + 8000L + i)));
            return e;
        });

        Map<String, Long> usuariosId = idsPorUsername();
        Map<String, Long> rolesId = idsPorNombre(Rol.class);
        String[][] asignaciones = {{"admin", "Administrador"}, {"gerente", "Gerente"}, {"supervisor01", "Supervisor"},
            {"caja01", "Cajero"}, {"caja02", "Cajero"}, {"caja03", "Cajero"}, {"caja04", "Cajero"},
            {"almacen01", "Almacenero"}, {"almacen02", "Almacenero"}, {"compras01", "Compras"},
            {"reponedor01", "Almacenero"}, {"reponedor02", "Almacenero"},
            {"admin", "Supervisor"}, {"supervisor01", "Compras"}};
        insertar(UsuarioRol.class, asignaciones.length, i -> {
            UsuarioRol e = new UsuarioRol();
            e.setUsuario(em.getReference(UsuarioSistema.class, usuariosId.get(asignaciones[i][0])));
            e.setRol(em.getReference(Rol.class, rolesId.get(asignaciones[i][1])));
            e.setFechaAsignacion(fechaRegistro(new Random(SEMILLA + 9000L + i)));
            e.setEstado(true);
            e.setUsuarioRegistro(i < 2 ? "admin" : "supervisor01");
            return e;
        });
    }

    void cargarCompras() {
        List<Long> proveedores = ids(Proveedor.class);
        List<Long> productos = ids(Producto.class);
        Random random = new Random(SEMILLA + 10000L);
        insertar(Compra.class, TOTAL_COMPRAS, i -> {
            Compra e = new Compra();
            e.setFecha(fechaHistorica(random));
            e.setProveedor(em.getReference(Proveedor.class, proveedores.get(random.nextInt(proveedores.size()))));
            e.setTipoComprobante("FACTURA");
            e.setNumeroComprobante(String.format("F%03d-%07d", 1 + i % 35, 1 + i / 35));
            e.setEstado(true);
            e.setProcesada(true);
            e.setUsuarioRegistro(i % 4 == 0 ? "almacen01" : "compras01");
            int cantidadDetalles = 5 + random.nextInt(8);
            Set<Integer> seleccionados = new HashSet<>();
            while (seleccionados.size() < cantidadDetalles) seleccionados.add(random.nextInt(TOTAL_PRODUCTOS));
            for (int productoIndice : seleccionados) {
                int cantidad = 12 + random.nextInt(49);
                DetalleCompra detalle = new DetalleCompra();
                detalle.setProducto(em.getReference(Producto.class, productos.get(productoIndice)));
                detalle.setCantidad(cantidad);
                detalle.setPrecio(catalogoProductos.get(productoIndice).precioCompra);
                e.agregarDetalle(detalle);
                entradas[productoIndice] += cantidad;
            }
            return e;
        });
    }

    void cargarVentas() {
        List<Long> clientes = ids(Cliente.class);
        List<Long> empleados = ids(Empleado.class);
        List<Long> metodos = ids(MetodoPago.class);
        List<Long> productos = ids(Producto.class);
        int[] cajeros = {2, 3, 4, 5, 6, 7};
        Random random = new Random(SEMILLA + 11000L);
        insertar(Venta.class, TOTAL_VENTAS, i -> {
            int clienteIndice = random.nextDouble() < 0.28 ? 0 : random.nextInt(clientes.size());
            boolean mayorista = clienteIndice > 0 && clienteIndice % 12 == 0;
            Venta e = new Venta();
            e.setFecha(fechaHistoricaConFinDeSemana(random));
            e.setCliente(em.getReference(Cliente.class, clientes.get(clienteIndice)));
            e.setEmpleado(em.getReference(Empleado.class, empleados.get(cajeros[random.nextInt(cajeros.length)])));
            e.setMetodoPago(em.getReference(MetodoPago.class, metodos.get(indiceMetodoPago(random.nextInt(100)))));
            e.setTipoComprobante(mayorista && i % 3 == 0 ? Venta.TipoComprobante.FACTURA
                : i % 15 == 0 ? Venta.TipoComprobante.TICKET : Venta.TipoComprobante.BOLETA);
            String serie = e.getTipoComprobante() == Venta.TipoComprobante.FACTURA ? "F001"
                : e.getTipoComprobante() == Venta.TipoComprobante.TICKET ? "T001" : "B001";
            e.setNumeroComprobante(String.format("%s-%08d", serie, i + 1));
            e.setProcesada(true);
            int cantidadDetalles = 2 + random.nextInt(5);
            Set<Integer> seleccionados = new HashSet<>();
            while (seleccionados.size() < cantidadDetalles) {
                int candidato = random.nextInt(TOTAL_PRODUCTOS);
                int disponible = STOCK_INICIAL + entradas[candidato] - salidas[candidato];
                if (disponible > 1) seleccionados.add(candidato);
            }
            for (int productoIndice : seleccionados) {
                int disponible = STOCK_INICIAL + entradas[productoIndice] - salidas[productoIndice];
                int cantidad = 1 + random.nextInt(Math.min(4, disponible - 1));
                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(em.getReference(Producto.class, productos.get(productoIndice)));
                detalle.setCantidad(cantidad);
                ProductoDato dato = catalogoProductos.get(productoIndice);
                detalle.setPrecio(mayorista ? dato.precioMayorista : dato.precioVenta);
                e.agregarDetalle(detalle);
                salidas[productoIndice] += cantidad;
            }
            return e;
        });
    }

    void actualizarStockFinal() {
        List<Long> productos = ids(Producto.class);
        for (int i = 0; i < productos.size(); i++) {
            if (!em.getTransaction().isActive()) em.getTransaction().begin();
            Producto producto = em.find(Producto.class, productos.get(i));
            producto.setStock(STOCK_INICIAL + entradas[i] - salidas[i]);
            if ((i + 1) % LOTE == 0 || i + 1 == productos.size()) {
                em.getTransaction().commit();
                em.clear();
            }
        }
    }

    <T> void insertar(Class<T> tipo, int cantidad, IntFunction<T> fabrica) {
        for (int i = 0; i < cantidad; i++) {
            if (!em.getTransaction().isActive()) em.getTransaction().begin();
            em.persist(fabrica.apply(i));
            if ((i + 1) % LOTE == 0 || i + 1 == cantidad) {
                em.getTransaction().commit();
                em.clear();
            }
        }
        System.out.printf("%-20s %,d%n", tipo.getSimpleName(), contar(tipo));
    }

    long contar(Class<?> tipo) {
        return em.createQuery("select count(e) from " + tipo.getSimpleName() + " e", Long.class).getSingleResult();
    }

    List<Long> ids(Class<?> tipo) {
        return em.createQuery("select e.id from " + tipo.getSimpleName() + " e order by e.id", Long.class).getResultList();
    }

    Map<String, Long> idsPorNombre(Class<?> tipo) {
        List<Object[]> filas = em.createQuery("select e.nombre, e.id from " + tipo.getSimpleName() + " e", Object[].class).getResultList();
        Map<String, Long> resultado = new HashMap<>();
        for (Object[] fila : filas) resultado.put((String) fila[0], (Long) fila[1]);
        return resultado;
    }

    Map<String, Long> idsPorUsername() {
        List<Object[]> filas = em.createQuery("select e.username, e.id from UsuarioSistema e", Object[].class).getResultList();
        Map<String, Long> resultado = new HashMap<>();
        for (Object[] fila : filas) resultado.put((String) fila[0], (Long) fila[1]);
        return resultado;
    }

    void imprimirConteos() {
        System.out.println("\nCONTEOS FINALES");
        Class<?>[] tipos = {Cargo.class, CategoriaProducto.class, Cliente.class, Compra.class, Empleado.class,
            Marca.class, MetodoPago.class, Producto.class, Proveedor.class, Rol.class, UsuarioRol.class,
            UsuarioSistema.class, Venta.class};
        for (Class<?> tipo : tipos) System.out.printf("%-20s %,d%n", tipo.getSimpleName(), contar(tipo));
        Number detallesCompra = (Number) em.createNativeQuery("SELECT COUNT(*) FROM DETALLE_COMPRA").getSingleResult();
        Number detallesVenta = (Number) em.createNativeQuery("SELECT COUNT(*) FROM DETALLE_VENTA").getSingleResult();
        System.out.printf("%-20s %,d%n", "DetalleCompra", detallesCompra.longValue());
        System.out.printf("%-20s %,d%n", "DetalleVenta", detallesVenta.longValue());
    }

    static int indiceMetodoPago(int porcentaje) {
        if (porcentaje < 42) return 0;
        if (porcentaje < 65) return 1;
        if (porcentaje < 75) return 2;
        if (porcentaje < 90) return 3;
        return 4;
    }

    static Date fechaRegistro(Random random) {
        LocalDate inicio = LocalDate.of(2023, 1, 1);
        LocalDate fin = LocalDate.of(2026, 7, 10);
        long dias = fin.toEpochDay() - inicio.toEpochDay();
        return Date.from(inicio.plusDays(random.nextInt((int) dias + 1)).atTime(9 + random.nextInt(9), random.nextInt(60))
            .atZone(ZoneId.systemDefault()).toInstant());
    }

    static Date fechaHistorica(Random random) {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2026, 7, 10);
        long dias = fin.toEpochDay() - inicio.toEpochDay();
        LocalDate fecha = inicio.plusDays(random.nextInt((int) dias + 1));
        return Date.from(fecha.atTime(7 + random.nextInt(15), random.nextInt(60), random.nextInt(60))
            .atZone(ZoneId.systemDefault()).toInstant());
    }

    static Date fechaHistoricaConFinDeSemana(Random random) {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2026, 7, 10);
        long dias = fin.toEpochDay() - inicio.toEpochDay();
        LocalDate fecha = inicio.plusDays(random.nextInt((int) dias + 1));
        if (fecha.getDayOfWeek().getValue() <= 4 && random.nextDouble() < 0.38) {
            fecha = fecha.plusDays(5 - fecha.getDayOfWeek().getValue() + random.nextInt(3));
            if (fecha.isAfter(fin)) fecha = fin.minusDays(random.nextInt(3));
        }
        return Date.from(fecha.atTime(7 + random.nextInt(16), random.nextInt(60), random.nextInt(60))
            .atZone(ZoneId.systemDefault()).toInstant());
    }

    static String correoCliente(String nombre, String paterno, String materno, int indice, Set<String> usados) {
        String primero = normalizar(nombre.split(" ")[0]);
        String local;
        switch (indice % 3) {
            case 0: local = primero + "." + normalizar(paterno); break;
            case 1: local = primero.charAt(0) + normalizar(paterno) + "." + normalizar(materno); break;
            default: local = primero + "." + normalizar(materno) + String.format("%02d", indice % 100); break;
        }
        String correo = local + "@gmail.com";
        if (!usados.add(correo)) {
            correo = local + indice + "@gmail.com";
            usados.add(correo);
        }
        return correo;
    }

    static String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    static String rucFicticio(int indice) {
        String base = "20" + String.format("%08d", 100000 + indice);
        int[] pesos = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;
        for (int i = 0; i < 10; i++) suma += Character.digit(base.charAt(i), 10) * pesos[i];
        int digito = 11 - suma % 11;
        if (digito == 10) digito = 0;
        else if (digito == 11) digito = 1;
        return base + digito;
    }

    static String[] nombres() {
        return new String[]{"Luis Alberto", "Maria Fernanda", "Jose Carlos", "Andrea Lucia", "Rosa Elena", "Miguel Angel",
            "Carmen Julia", "Diego Alonso", "Ana Sofia", "Jorge Luis", "Fiorella Alejandra", "Marco Antonio",
            "Lucia Beatriz", "Carlos Eduardo", "Daniela Isabel", "Pedro Enrique", "Valeria Nicole", "Juan Diego",
            "Paola Andrea", "Renato Sebastian", "Claudia Patricia", "Victor Manuel", "Diana Carolina", "Raul Alejandro",
            "Milagros del Pilar", "Sergio Andres", "Elena Maribel", "Fernando Javier", "Gabriela Estefania", "Oscar Ivan"};
    }

    static String[] apellidosPaternos() {
        return new String[]{"Mendoza", "Rojas", "Vasquez", "Fernandez", "Paredes", "Ramirez", "Sanchez", "Castillo",
            "Flores", "Torres", "Chavez", "Diaz", "Huaman", "Vargas", "Lopez", "Garcia", "Rodriguez", "Salazar",
            "Cruz", "Medina", "Espinoza", "Reyes", "Aguilar", "Campos", "Cabrera", "Silva", "Herrera", "Nunez", "Ortiz", "Peña"};
    }

    static String[] apellidosMaternos() {
        return new String[]{"Salazar", "Torres", "Ruiz", "Diaz", "Flores", "Huaman", "Vargas", "Chavez", "Mendoza",
            "Rojas", "Paredes", "Ramirez", "Sanchez", "Castillo", "Vasquez", "Fernandez", "Lopez", "Garcia",
            "Rodriguez", "Cruz", "Medina", "Espinoza", "Reyes", "Aguilar", "Campos", "Cabrera", "Silva", "Herrera", "Nunez", "Ortiz"};
    }

    static List<FamiliaProducto> familiasProducto() {
        return Arrays.asList(
            f("Gaseosa", "Bebidas gaseosas", "2.10", a("Inca Kola", "Coca-Cola", "Pepsi", "Kola Real"), a("original", "sin azucar", "personal", "retornable"), a("355 ml", "500 ml", "600 ml", "1.5 L")),
            f("Agua", "Agua", "1.00", a("San Luis", "Cielo", "San Mateo", "Vida"), a("sin gas", "con gas", "mineral", "purificada"), a("500 ml", "625 ml", "1 L", "2.5 L")),
            f("Nectar", "Jugos", "1.40", a("Frugos", "Pulp", "Gloria", "Watts"), a("durazno", "mango", "manzana", "naranja"), a("300 ml", "500 ml", "1 L", "1.5 L")),
            f("Energizante", "Bebidas energeticas", "3.80", a("Red Bull", "Volt", "Monster", "220V"), a("original", "tropical", "sin azucar", "citrico"), a("250 ml", "300 ml", "473 ml", "500 ml")),
            f("Leche evaporada", "Lacteos", "3.20", a("Gloria", "Ideal", "Laive", "Bonle"), a("entera", "light", "sin lactosa", "ninos"), a("390 g", "400 g", "410 g", "pack 6")),
            f("Yogurt", "Lacteos", "2.40", a("Gloria", "Laive", "Pura Vida", "Milkito"), a("fresa", "vainilla", "durazno", "natural"), a("180 g", "500 g", "946 ml", "1 kg")),
            f("Queso", "Lacteos", "5.80", a("Bonle", "Laive", "Gloria", "Delice"), a("fresco", "edam", "andino", "mozzarella"), a("200 g", "250 g", "400 g", "500 g")),
            f("Pan de molde", "Panaderia", "4.10", a("Bimbo", "Union", "Pyc", "San Jorge"), a("blanco", "integral", "multicereal", "sin corteza"), a("400 g", "480 g", "550 g", "650 g")),
            f("Galletas", "Galletas", "0.70", a("Oreo", "Field", "Soda V", "Casino"), a("original", "chocolate", "vainilla", "fresa"), a("36 g", "60 g", "100 g", "pack 6")),
            f("Papas", "Snacks", "1.50", a("Lays", "Inka Chips", "Pringles", "Mr Chips"), a("clasicas", "sal de mar", "barbecue", "picantes"), a("40 g", "75 g", "135 g", "160 g")),
            f("Chifles", "Snacks", "1.20", a("Karinto", "Inka Crops", "Tortees", "Villa Natura"), a("salados", "picantes", "dulces", "limon"), a("50 g", "100 g", "150 g", "250 g")),
            f("Chocolate", "Chocolates", "1.00", a("Sublime", "Triangulo", "Princesa", "Iberica"), a("clasico", "mani", "almendras", "bitter"), a("30 g", "50 g", "80 g", "100 g")),
            f("Caramelos", "Golosinas", "0.80", a("Ambrosoli", "Arcor", "Sayori", "Halls"), a("menta", "fresa", "limon", "surtidos"), a("50 g", "100 g", "150 g", "bolsa 250 g")),
            f("Cereal", "Cereales", "5.50", a("Angel", "Kelloggs", "Nestle", "Tottus"), a("hojuelas", "chocolate", "miel", "frutos rojos"), a("200 g", "300 g", "500 g", "700 g")),
            f("Atun", "Conservas", "3.40", a("Florida", "Primor", "Campomar", "A1"), a("en aceite", "en agua", "filete", "trozos"), a("140 g", "160 g", "170 g", "pack 3")),
            f("Sardina", "Conservas", "2.40", a("Florida", "Campomar", "A1", "Portola"), a("en tomate", "en aceite", "picante", "natural"), a("155 g", "170 g", "200 g", "425 g")),
            f("Fideos", "Fideos", "1.80", a("Don Vittorio", "Molitalia", "Lavaggi", "Nicolini"), a("spaghetti", "tallarin", "tornillo", "cabello de angel"), a("250 g", "500 g", "750 g", "1 kg")),
            f("Arroz", "Arroz", "3.20", a("Costeño", "Paisana", "Valle Norte", "Faraon"), a("extra", "superior", "integral", "añejo"), a("750 g", "1 kg", "2 kg", "5 kg")),
            f("Azucar", "Azucar", "2.40", a("Cartavio", "Casa Grande", "Paramonga", "Dulfina"), a("rubia", "blanca", "organica", "impalpable"), a("500 g", "1 kg", "2 kg", "5 kg")),
            f("Aceite", "Aceite", "5.20", a("Primor", "Cocinero", "Sao", "Friol"), a("vegetal", "girasol", "canola", "soya"), a("500 ml", "900 ml", "1 L", "1.8 L")),
            f("Salsa", "Condimentos", "1.50", a("Alacena", "Maggi", "Sibarita", "Kikko"), a("mayonesa", "ketchup", "mostaza", "sillao"), a("100 g", "250 g", "400 g", "500 g")),
            f("Jamon", "Embutidos", "4.50", a("San Fernando", "Braedt", "Otto Kunz", "La Segoviana"), a("ingles", "pavita", "ahumado", "pierna"), a("100 g", "200 g", "250 g", "500 g")),
            f("Salchicha", "Embutidos", "3.50", a("San Fernando", "Braedt", "Otto Kunz", "Laive"), a("hot dog", "vienesa", "pollo", "ahumada"), a("200 g", "250 g", "500 g", "pack 12")),
            f("Nuggets", "Congelados", "7.50", a("San Fernando", "Redondos", "Avinka", "Otto Kunz"), a("pollo", "queso", "picantes", "clasicos"), a("250 g", "400 g", "500 g", "1 kg")),
            f("Helado", "Helados", "2.00", a("Donofrio", "Artika", "Yamboly", "Lamborghini"), a("vainilla", "chocolate", "fresa", "lucuma"), a("vaso 100 ml", "cono", "500 ml", "1 L")),
            f("Shampoo", "Higiene personal", "7.00", a("Head Shoulders", "Pantene", "Sedal", "Savital"), a("limpieza", "hidratacion", "anticaspa", "reparacion"), a("180 ml", "375 ml", "650 ml", "750 ml")),
            f("Jabon", "Higiene personal", "1.30", a("Protex", "Dove", "Rexona", "Palmolive"), a("antibacterial", "avena", "aloe", "neutro"), a("75 g", "90 g", "110 g", "pack 3")),
            f("Detergente", "Limpieza del hogar", "3.20", a("Bolivar", "Ariel", "Ace", "Marsella"), a("limon", "floral", "multiaccion", "lavanda"), a("450 g", "780 g", "1.2 kg", "2.6 kg")),
            f("Lavavajilla", "Limpieza del hogar", "2.00", a("Sapolio", "Ayudin", "Patito", "Trome"), a("limon", "aloe", "antibacterial", "naranja"), a("300 ml", "500 ml", "750 ml", "1 L")),
            f("Papel higienico", "Papel higienico", "3.50", a("Elite", "Suave", "Paracas", "Noble"), a("doble hoja", "triple hoja", "aromatizado", "clasico"), a("2 rollos", "4 rollos", "6 rollos", "12 rollos")),
            f("Pañales", "Cuidado infantil", "7.50", a("Huggies", "Pampers", "Babysec", "Pequeñin"), a("recien nacido", "confort", "proteccion", "noche"), a("talla P", "talla M", "talla G", "talla XG")),
            f("Comida para perro", "Productos para mascotas", "4.20", a("Ricocan", "Mimaskot", "Dog Chow", "Pedigree"), a("adultos", "cachorros", "carne", "pollo"), a("500 g", "1 kg", "2 kg", "3 kg")),
            f("Sandwich", "Comida preparada", "3.80", a("Tambo", "Listo", "Casa Andina", "Buen Sabor"), a("pollo", "jamon y queso", "triple", "vegetal"), a("unidad", "pack 2", "180 g", "250 g")),
            f("Cafe", "Cafe e infusiones", "4.00", a("Altomayo", "Nescafe", "Villa Rica", "Cafetal"), a("clasico", "instantaneo", "premium", "descafeinado"), a("50 g", "100 g", "170 g", "250 g")),
            f("Te filtrante", "Cafe e infusiones", "2.30", a("McColins", "Hornimans", "Wawasana", "Herbi"), a("manzanilla", "anis", "canela", "hierba luisa"), a("20 sobres", "25 sobres", "50 sobres", "caja surtida"))
        );
    }

    static FamiliaProducto f(String tipo, String categoria, String precio, String[] marcas, String[] variedades, String[] presentaciones) {
        return new FamiliaProducto(tipo, categoria, new BigDecimal(precio), marcas, variedades, presentaciones);
    }

    static String[] a(String... valores) {
        return valores;
    }

    static final class FamiliaProducto {
        final String tipo;
        final String categoria;
        final BigDecimal precioBase;
        final String[] marcas;
        final String[] variedades;
        final String[] presentaciones;

        FamiliaProducto(String tipo, String categoria, BigDecimal precioBase, String[] marcas, String[] variedades, String[] presentaciones) {
            this.tipo = tipo;
            this.categoria = categoria;
            this.precioBase = precioBase;
            this.marcas = marcas;
            this.variedades = variedades;
            this.presentaciones = presentaciones;
        }
    }

    static final class ProductoDato {
        final String nombre;
        final String descripcion;
        final String categoria;
        final String marca;
        final BigDecimal precioCompra;
        final BigDecimal precioVenta;
        final BigDecimal precioMayorista;

        ProductoDato(String nombre, String descripcion, String categoria, String marca, BigDecimal precioCompra,
                BigDecimal precioVenta, BigDecimal precioMayorista) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.categoria = categoria;
            this.marca = marca;
            this.precioCompra = precioCompra;
            this.precioVenta = precioVenta;
            this.precioMayorista = precioMayorista;
        }
    }
}
