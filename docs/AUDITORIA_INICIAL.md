# Auditoria inicial

Fecha de auditoria: 10 de julio de 2026. Estado observado antes de continuar el trabajo inconcluso de Claude.

| Criterio | Estado inicial | Evidencia | Problema | Accion requerida |
|---|---|---|---|---|
| Git | Parcial | Rama `main`, commit `66f6661`, 17 archivos fuente/i18n modificados | Cambios de Claude sin punto de restauracion | Crear rama y commit de respaldo |
| Compilacion | Cumplia | `mvn clean compile`: BUILD SUCCESS | Solo validaba las correcciones parciales | Repetir tras cada fase |
| Java | Parcial | `java -version`: 20; `mvn -version`: Java 25.0.2 | Entorno inconsistente | Fijar `JAVA_HOME` y documentar |
| OpenXava | Cumplia | `pom.xml`: 7.7.3 | Sin problema de version | Conservar compatibilidad |
| Empaquetado | Parcial | `packaging=war` | WAR previo eliminado por `clean` | Regenerar y probar |
| Modelo JPA | Parcial | 13 `@Entity`, 2 detalles en conversion a `@Embeddable` | Faltaban restricciones, indices y reglas transaccionales | Completar modelo y servicios |
| Dinero | Parcial | Claude habia cambiado `double` a `BigDecimal` | Tests antiguos aun usaban `double` | Migrar tests y fijar precision |
| Maestro-detalle | Parcial | `Compra` y `Venta` con `@ElementCollection` | Sin logica de stock ni estado de proceso | Agregar servicios y acciones |
| Base de datos | No cumplia | HSQLDB local con tablas `TUPRIMERAENTIDAD`, `ACOPIO`, `PRODUCTOR`, `VARIEDAD` | Datos de ejemplo ajenos y esquema antiguo | Respaldar, limpiar y recrear |
| Configuracion BD | No cumplia | `context.xml` tenia recursos duplicados y cierre de comentario suelto; POM incluia MySQL | Motor declarado inconsistente | Unificar en HSQLDB 2.7.4 |
| Integridad | Parcial | PK en entidades y algunas FK | Sin indices/UNIQUE completos; detalles antiguos eran entidades | Recrear esquema desde JPA |
| Pruebas | No cumplia | 4 archivos JUnit 5, uno vacio y tres incompatibles con `BigDecimal` | Contradecia JUnit 4 exigido por `AGENTS.md` | Reescribir con `ModuleTestBase` |
| Mockito | No cumplia | Dependencia presente, sin pruebas Mockito reales | No habia dependencia aislable | Introducir puerto de repositorio y pruebas |
| JaCoCo | Parcial | Plugin 0.8.14, sin regla minima | No habia verificacion de 80 % | Configurar `check` y documentar ejecucion IDE |
| Carga de datos | No cumplia | Entre 1 y 10 filas por tabla | Muy lejos de 6,000 por entidad | Crear cargador idempotente |
| Tomcat | Parcial | Tomcat embebido transitivo | No existia evidencia de WAR/HTTP | Empaquetar y hacer smoke test |
| Documentacion | No cumplia | Sin `README.md` ni `docs/` | No satisfacia lista ni rubrica | Crear manuales, matrices e informe |
| Seguridad | Parcial | Cuenta demo `admin/admin`; sin secretos reales | Credencial predeterminada solo apta para local | Advertir cambio y no usar produccion |

## Inventario inicial

- Entidades: Cargo, CategoriaProducto, Cliente, Compra, Empleado, Marca, MetodoPago, Producto, Proveedor, Rol, UsuarioRol, UsuarioSistema y Venta.
- Embebibles: DetalleCompra y DetalleVenta.
- Acciones propias iniciales: ninguna.
- Servicios/DAO iniciales: ninguno.
- Motor real: HSQLDB, pese a una dependencia MySQL sin uso.
- Maven: proyecto unico, WAR, sin submodulos.

## Comandos de auditoria

Se ejecutaron `java -version`, `mvn -version`, `git status`, `mvn clean compile` y `mvn dependency:tree`. No se ejecuto `mvn test` porque `AGENTS.md` indica expresamente que las pruebas deben lanzarse desde el IDE.
