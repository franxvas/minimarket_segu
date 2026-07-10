# Minimarket Segu

Sistema web academico para administrar productos, inventario, clientes, proveedores, compras, ventas, empleados, usuarios y roles de un minimarket. Esta construido con OpenXava, JPA/Hibernate, HSQLDB y Maven, y se empaqueta como WAR para Tomcat 9.

## Funciones principales

- CRUD generado por OpenXava para las 13 entidades persistentes.
- Compras maestro-detalle que incrementan existencias mediante la accion **Procesar**.
- Ventas maestro-detalle que validan y descuentan existencias sin permitir stock negativo.
- Precios monetarios con `BigDecimal`, validaciones de DNI, RUC, correo, telefono, cantidades y montos.
- Alertas consultables mediante `stock`, `stockMinimo` y `bajoStock`.
- Carga realista y repetible con 6,000 ventas; catalogos y maestros dimensionados segun su naturaleza.
- Pruebas JUnit 4, pruebas funcionales OpenXava y Mockito.

## Versiones verificadas

| Componente | Version |
|---|---:|
| OpenXava | 7.7.3 |
| Java de Maven | 25.0.2 |
| Maven | 3.9.15 |
| Hibernate ORM | 5.6.15.Final |
| HSQLDB | 2.7.4 |
| Tomcat compatible | 9.0.x (embebido verificado: 9.0.118) |

La orden `java -version` del sistema apunta a Java 20, mientras Maven usa Java 25. Se recomienda definir `JAVA_HOME` al JDK 25 antes de trabajar.

## Inicio rapido

```bash
export JAVA_HOME=/opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home
mvn clean compile
mvn package -DskipTests -Djacoco.skip=true
mvn exec:java
```

Abra `http://localhost:8080/minimarket_segu/`. La cuenta ficticia inicial de desarrollo es `admin` / `admin`; debe cambiarse fuera de una demostracion local.

## Base de datos y carga academica

La base versionada se encuentra en `data/minimarket_segu-db.*`. Para reconstruir la carga realista con el servidor detenido se exige doble confirmacion:

```bash
mvn compile
MINIMARKET_SEED_CONFIRM=RESET_REALISTIC_DATA mvn exec:java \
  -Dexec.mainClass=com.minimarket.minimarket_segu.datos.CargaMasivaDatos \
  -Dexec.classpathScope=runtime -Dexec.args=--reset-and-seed
mvn exec:java -Dexec.mainClass=com.minimarket.minimarket_segu.datos.VerificarBaseDatos -Dexec.classpathScope=runtime
```

El generador no se ejecuta durante el arranque normal. Los datos son sinteticos y no identifican personas ni empresas reales.

## Pruebas

Las pruebas siguen JUnit 4 y `ModuleTestBase`. Inicie primero la aplicacion y ejecute `mvn verify`: se verificaron 21 pruebas y 82.48 % de cobertura de lineas. Cada metodo comienza con `login("admin", "admin")`. Consulte [GUIA_PRUEBAS.md](docs/GUIA_PRUEBAS.md).

## Despliegue externo

Use Tomcat 9, no Tomcat 10/11, porque OpenXava 7.7.3 trabaja con `javax.*`. Los scripts estan en `scripts/tomcat/` y la guia detallada en [GUIA_DESPLIEGUE_TOMCAT.md](docs/GUIA_DESPLIEGUE_TOMCAT.md).

## Estructura

- `src/main/java/.../modelo`: entidades y embebibles JPA.
- `src/main/java/.../servicios`: reglas y persistencia de inventario.
- `src/main/java/.../acciones`: acciones OpenXava de compras y ventas.
- `src/main/java/.../datos`: carga y verificacion reproducible.
- `src/test/java/.../pruebas`: JUnit 4, CRUD y Mockito.
- `scripts/`: base de datos y Tomcat.
- `docs/`: auditorias, manuales, informe, diagramas y evidencias.

## Integrantes

1. [COMPLETAR INTEGRANTE 1]
2. [COMPLETAR INTEGRANTE 2]
3. [COMPLETAR INTEGRANTE 3]
4. [COMPLETAR INTEGRANTE 4]
5. [COMPLETAR INTEGRANTE 5]
6. [COMPLETAR INTEGRANTE 6]
