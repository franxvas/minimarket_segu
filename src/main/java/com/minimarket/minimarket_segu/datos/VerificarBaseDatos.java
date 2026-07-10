package com.minimarket.minimarket_segu.datos;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/** Ejecuta controles reproducibles de cantidad, calidad, unicidad e integridad. */
public final class VerificarBaseDatos {

    static final String URL = "jdbc:hsqldb:file:data/minimarket_segu-db;shutdown=true";
    static int errores;

    public static void main(String[] args) throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        try (Connection connection = DriverManager.getConnection(URL, "sa", "")) {
            imprimirConteos(connection);
            imprimirCalidad(connection);
            imprimirMetricas(connection);
        }
        if (errores > 0) throw new IllegalStateException("La validacion termino con " + errores + " control(es) fallidos");
        System.out.println("\nRESULTADO_FINAL,VALIDACION_COMPLETA,OK");
    }

    static void imprimirConteos(Connection connection) throws SQLException {
        Map<String, Rango> esperados = new LinkedHashMap<>();
        esperados.put("CARGO", exacto(8));
        esperados.put("CATEGORIAPRODUCTO", exacto(28));
        esperados.put("CLIENTE", exacto(3000));
        esperados.put("COMPRA", exacto(900));
        esperados.put("DETALLE_COMPRA", new Rango(4500, 10800));
        esperados.put("EMPLEADO", exacto(24));
        esperados.put("MARCA", exacto(120));
        esperados.put("METODOPAGO", exacto(5));
        esperados.put("PRODUCTO", exacto(1800));
        esperados.put("PROVEEDOR", exacto(80));
        esperados.put("ROL", exacto(6));
        esperados.put("USUARIOROL", exacto(14));
        esperados.put("USUARIOSISTEMA", exacto(12));
        esperados.put("VENTA", exacto(6000));
        esperados.put("DETALLE_VENTA", new Rango(12000, 36000));

        System.out.println("ENTIDAD,ESPERADO,ENCONTRADO,ESTADO");
        for (Map.Entry<String, Rango> entry : esperados.entrySet()) {
            long total = valor(connection, "SELECT COUNT(*) FROM " + entry.getKey());
            boolean valido = entry.getValue().contiene(total);
            if (!valido) errores++;
            System.out.printf("%s,%s,%d,%s%n", entry.getKey(), entry.getValue(), total, valido ? "OK" : "ERROR");
        }
    }

    static void imprimirCalidad(Connection connection) throws SQLException {
        System.out.println("\nCONTROL,RESULTADO,ESPERADO,ESTADO");

        controlCero(connection, "catalogos_con_nombres_duplicados",
            "SELECT COUNT(*) FROM (" +
            "SELECT LOWER(TRIM(NOMBRE)) N FROM CARGO GROUP BY LOWER(TRIM(NOMBRE)) HAVING COUNT(*) > 1 " +
            "UNION ALL SELECT LOWER(TRIM(NOMBRE)) FROM CATEGORIAPRODUCTO GROUP BY LOWER(TRIM(NOMBRE)) HAVING COUNT(*) > 1 " +
            "UNION ALL SELECT LOWER(TRIM(NOMBRE)) FROM MARCA GROUP BY LOWER(TRIM(NOMBRE)) HAVING COUNT(*) > 1 " +
            "UNION ALL SELECT LOWER(TRIM(NOMBRE)) FROM METODOPAGO GROUP BY LOWER(TRIM(NOMBRE)) HAVING COUNT(*) > 1 " +
            "UNION ALL SELECT LOWER(TRIM(NOMBRE)) FROM ROL GROUP BY LOWER(TRIM(NOMBRE)) HAVING COUNT(*) > 1)");
        controlCero(connection, "productos_nombre_duplicado", duplicados("PRODUCTO", "NOMBRE", false));
        controlCero(connection, "clientes_dni_duplicado", duplicados("CLIENTE", "DNI", false));
        controlCero(connection, "clientes_ruc_duplicado", duplicados("CLIENTE", "RUC", true));
        controlCero(connection, "clientes_correo_duplicado", duplicados("CLIENTE", "CORREO", true));
        controlCero(connection, "empleados_dni_duplicado", duplicados("EMPLEADO", "DNI", false));
        controlCero(connection, "empleados_correo_duplicado", duplicados("EMPLEADO", "CORREO", true));
        controlCero(connection, "proveedores_ruc_duplicado", duplicados("PROVEEDOR", "RUC", false));
        controlCero(connection, "proveedores_correo_duplicado", duplicados("PROVEEDOR", "CORREO", true));
        controlCero(connection, "usuarios_username_duplicado", duplicados("USUARIOSISTEMA", "USERNAME", false));
        controlCero(connection, "asignaciones_rol_duplicadas",
            "SELECT COUNT(*) FROM (SELECT USUARIO_ID, ROL_ID FROM USUARIOROL GROUP BY USUARIO_ID, ROL_ID HAVING COUNT(*) > 1)");

        controlCero(connection, "clientes_obligatorios_faltantes",
            "SELECT COUNT(*) FROM CLIENTE WHERE NOMBRE IS NULL OR TRIM(NOMBRE)='' OR DNI IS NULL OR LENGTH(DNI)<>8 OR TIPOCLIENTE IS NULL");
        controlCero(connection, "proveedores_obligatorios_faltantes",
            "SELECT COUNT(*) FROM PROVEEDOR WHERE NOMBRE IS NULL OR TRIM(NOMBRE)='' OR RUC IS NULL OR LENGTH(RUC)<>11");
        controlCero(connection, "productos_obligatorios_faltantes",
            "SELECT COUNT(*) FROM PRODUCTO WHERE NOMBRE IS NULL OR TRIM(NOMBRE)='' OR CATEGORIA_ID IS NULL OR MARCA_ID IS NULL");
        controlCero(connection, "clientes_correo_no_gmail",
            "SELECT COUNT(*) FROM CLIENTE WHERE CORREO IS NOT NULL AND LOWER(CORREO) NOT LIKE '%@gmail.com'");
        controlCero(connection, "empleados_correo_no_gmail",
            "SELECT COUNT(*) FROM EMPLEADO WHERE CORREO IS NOT NULL AND LOWER(CORREO) NOT LIKE '%@gmail.com'");
        controlCero(connection, "proveedores_correo_no_gmail",
            "SELECT COUNT(*) FROM PROVEEDOR WHERE CORREO IS NOT NULL AND LOWER(CORREO) NOT LIKE '%@gmail.com'");
        controlCero(connection, "textos_artificiales_residuales",
            "SELECT COUNT(*) FROM (" +
            "SELECT NOMBRE FROM CLIENTE WHERE LOWER(NOMBRE) LIKE '%cliente demo%' OR LOWER(NOMBRE) LIKE '%carga academica%' " +
            "UNION ALL SELECT NOMBRE FROM PRODUCTO WHERE LOWER(NOMBRE) LIKE '%producto demo%' OR LOWER(NOMBRE) LIKE '%carga academica%' " +
            "UNION ALL SELECT NOMBRE FROM PROVEEDOR WHERE LOWER(NOMBRE) LIKE '%proveedor demo%' OR LOWER(NOMBRE) LIKE '%carga academica%')");

        controlCero(connection, "productos_stock_negativo", "SELECT COUNT(*) FROM PRODUCTO WHERE STOCK < 0");
        controlCero(connection, "productos_precio_invalido",
            "SELECT COUNT(*) FROM PRODUCTO WHERE PRECIOVENTA <= 0 OR PRECIOCOMPRA < 0 OR PRECIOMAYORISTA < 0 " +
            "OR PRECIOVENTA < PRECIOCOMPRA OR PRECIOMAYORISTA < PRECIOCOMPRA");
        controlCero(connection, "detalles_compra_invalidos", "SELECT COUNT(*) FROM DETALLE_COMPRA WHERE CANTIDAD <= 0 OR PRECIO <= 0");
        controlCero(connection, "detalles_venta_invalidos", "SELECT COUNT(*) FROM DETALLE_VENTA WHERE CANTIDAD <= 0 OR PRECIO <= 0");
        controlCero(connection, "compras_sin_detalle",
            "SELECT COUNT(*) FROM COMPRA c WHERE NOT EXISTS (SELECT 1 FROM DETALLE_COMPRA d WHERE d.COMPRA_ID=c.ID)");
        controlCero(connection, "ventas_sin_detalle",
            "SELECT COUNT(*) FROM VENTA v WHERE NOT EXISTS (SELECT 1 FROM DETALLE_VENTA d WHERE d.VENTA_ID=v.ID)");
        controlCero(connection, "compras_no_procesadas", "SELECT COUNT(*) FROM COMPRA WHERE PROCESADA=FALSE");
        controlCero(connection, "ventas_no_procesadas", "SELECT COUNT(*) FROM VENTA WHERE PROCESADA=FALSE");

        controlCero(connection, "empleados_sin_cargo",
            "SELECT COUNT(*) FROM EMPLEADO e LEFT JOIN CARGO c ON c.ID=e.CARGO_ID WHERE c.ID IS NULL");
        controlCero(connection, "productos_sin_categoria",
            "SELECT COUNT(*) FROM PRODUCTO p LEFT JOIN CATEGORIAPRODUCTO c ON c.ID=p.CATEGORIA_ID WHERE c.ID IS NULL");
        controlCero(connection, "productos_sin_marca",
            "SELECT COUNT(*) FROM PRODUCTO p LEFT JOIN MARCA m ON m.ID=p.MARCA_ID WHERE m.ID IS NULL");
        controlCero(connection, "usuarios_sin_empleado",
            "SELECT COUNT(*) FROM USUARIOSISTEMA u LEFT JOIN EMPLEADO e ON e.ID=u.EMPLEADO_ID WHERE e.ID IS NULL");
        controlCero(connection, "roles_usuario_huerfanos",
            "SELECT COUNT(*) FROM USUARIOROL ur LEFT JOIN USUARIOSISTEMA u ON u.ID=ur.USUARIO_ID " +
            "LEFT JOIN ROL r ON r.ID=ur.ROL_ID WHERE u.ID IS NULL OR r.ID IS NULL");
        controlCero(connection, "compras_huerfanas",
            "SELECT COUNT(*) FROM COMPRA c LEFT JOIN PROVEEDOR p ON p.ID=c.PROVEEDOR_ID WHERE p.ID IS NULL");
        controlCero(connection, "ventas_huerfanas",
            "SELECT COUNT(*) FROM VENTA v LEFT JOIN CLIENTE c ON c.ID=v.CLIENTE_ID " +
            "LEFT JOIN EMPLEADO e ON e.ID=v.EMPLEADO_ID LEFT JOIN METODOPAGO m ON m.ID=v.METODOPAGO_ID " +
            "WHERE c.ID IS NULL OR e.ID IS NULL OR m.ID IS NULL");
        controlCero(connection, "detalles_compra_huerfanos",
            "SELECT COUNT(*) FROM DETALLE_COMPRA d LEFT JOIN COMPRA c ON c.ID=d.COMPRA_ID " +
            "LEFT JOIN PRODUCTO p ON p.ID=d.PRODUCTO_ID WHERE c.ID IS NULL OR p.ID IS NULL");
        controlCero(connection, "detalles_venta_huerfanos",
            "SELECT COUNT(*) FROM DETALLE_VENTA d LEFT JOIN VENTA v ON v.ID=d.VENTA_ID " +
            "LEFT JOIN PRODUCTO p ON p.ID=d.PRODUCTO_ID WHERE v.ID IS NULL OR p.ID IS NULL");
        controlCero(connection, "stock_no_concilia",
            "SELECT COUNT(*) FROM PRODUCTO p WHERE p.STOCK <> " + CargaMasivaDatos.STOCK_INICIAL +
            " + COALESCE((SELECT SUM(dc.CANTIDAD) FROM DETALLE_COMPRA dc WHERE dc.PRODUCTO_ID=p.ID),0) " +
            "- COALESCE((SELECT SUM(dv.CANTIDAD) FROM DETALLE_VENTA dv WHERE dv.PRODUCTO_ID=p.ID),0)");

        controlExacto(connection, "metodos_pago_no_permitidos",
            "SELECT COUNT(*) FROM METODOPAGO WHERE NOMBRE NOT IN ('Efectivo','Yape','Plin','Tarjeta de debito','Tarjeta de credito')", 0);
        controlExacto(connection, "clientes_sin_correo_en_rango",
            "SELECT CASE WHEN COUNT(*) BETWEEN 900 AND 1200 THEN 1 ELSE 0 END FROM CLIENTE WHERE CORREO IS NULL", 1);
    }

    static void imprimirMetricas(Connection connection) throws SQLException {
        System.out.println("\nMETRICA,VALOR");
        metrica(connection, "clientes_con_correo", "SELECT COUNT(*) FROM CLIENTE WHERE CORREO IS NOT NULL");
        metrica(connection, "clientes_sin_correo", "SELECT COUNT(*) FROM CLIENTE WHERE CORREO IS NULL");
        metrica(connection, "clientes_mayoristas", "SELECT COUNT(*) FROM CLIENTE WHERE TIPOCLIENTE='MAYORISTA'");
        metrica(connection, "fecha_primera_venta", "SELECT MIN(FECHA) FROM VENTA");
        metrica(connection, "fecha_ultima_venta", "SELECT MAX(FECHA) FROM VENTA");
        metrica(connection, "dias_distintos_con_ventas", "SELECT COUNT(DISTINCT CAST(FECHA AS DATE)) FROM VENTA");
        metrica(connection, "total_unidades_compradas", "SELECT SUM(CANTIDAD) FROM DETALLE_COMPRA");
        metrica(connection, "total_unidades_vendidas", "SELECT SUM(CANTIDAD) FROM DETALLE_VENTA");
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT m.NOMBRE, COUNT(*) FROM VENTA v JOIN METODOPAGO m ON m.ID=v.METODOPAGO_ID GROUP BY m.NOMBRE ORDER BY m.NOMBRE")) {
            while (rs.next()) System.out.printf("ventas_%s,%s%n", rs.getString(1).replace(' ', '_'), rs.getString(2));
        }
    }

    static String duplicados(String tabla, String columna, boolean omitirNulos) {
        return "SELECT COUNT(*) FROM (SELECT LOWER(TRIM(" + columna + ")) V FROM " + tabla
            + (omitirNulos ? " WHERE " + columna + " IS NOT NULL" : "")
            + " GROUP BY LOWER(TRIM(" + columna + ")) HAVING COUNT(*) > 1)";
    }

    static void controlCero(Connection connection, String nombre, String sql) throws SQLException {
        controlExacto(connection, nombre, sql, 0);
    }

    static void controlExacto(Connection connection, String nombre, String sql, long esperado) throws SQLException {
        long resultado = valor(connection, sql);
        boolean valido = resultado == esperado;
        if (!valido) errores++;
        System.out.printf("%s,%d,%d,%s%n", nombre, resultado, esperado, valido ? "OK" : "ERROR");
    }

    static long valor(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    static void metrica(Connection connection, String nombre, String sql) throws SQLException {
        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            System.out.printf("%s,%s%n", nombre, rs.getString(1));
        }
    }

    static Rango exacto(long valor) {
        return new Rango(valor, valor);
    }

    static final class Rango {
        final long minimo;
        final long maximo;

        Rango(long minimo, long maximo) {
            this.minimo = minimo;
            this.maximo = maximo;
        }

        boolean contiene(long valor) {
            return valor >= minimo && valor <= maximo;
        }

        public String toString() {
            return minimo == maximo ? Long.toString(minimo) : minimo + "-" + maximo;
        }
    }
}
