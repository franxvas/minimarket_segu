# Sistema de gestion Minimarket Segu

**Universidad:** Universidad Nacional Toribio Rodriguez de Mendoza de Amazonas  
**Facultad:** Ingenieria de Sistemas y Mecanica Electrica  
**Escuela:** Ingenieria de Sistemas  
**Curso:** Taller de Programacion II  
**Docente:** LUCIANO LLAUCE  
**Integrantes:** 
1. ANGULO GRANDEZ KATYA
2. CAMPOS MARCO CRISTIAN LENIN
3. CAJUSOL BUSTAMANTE JOSE ANTONIO
4. CHASQUERO CASTILLO SEGUNDO NORBERTO
5. MOSQUEDA VALLEJOS DONALD IVAN
6. VASQUEZ SALDAÑA FRANCO ARON
**PYME:** MINIMARKET SEGU 
**Fecha:** 2026

## Indice

1. Introduccion y diagnostico
2. Requerimientos, objetivos y alcance
3. Diseno y arquitectura
4. Implementacion y modulos
5. Pruebas, datos y despliegue
6. Costos, beneficios e indicadores
7. Conclusiones, recomendaciones y anexos

## 1. Introduccion

El proyecto propone una aplicacion web para centralizar operaciones habituales de un minimarket: catalogo de productos, existencias, clientes, proveedores, compras, ventas y control basico de usuarios. La solucion usa OpenXava para generar una interfaz empresarial a partir del modelo Java, mientras JPA/Hibernate mantiene la integridad del esquema HSQLDB.

## 2. Descripcion y diagnostico de la PYME

La identidad, ubicacion, numero de trabajadores, volumen de transacciones y herramientas actuales de la PYME deben completarse con informacion comprobable: **[COMPLETAR CON INFORMACION DE LA PYME]**. No se atribuyen entrevistas ni cifras inexistentes.

El diagnostico funcional parte de un riesgo comun y observable en el dominio: cuando productos, compras y ventas se registran de manera separada, el stock deja de ser confiable y se dificulta responder que reponer, que se vendio y quien suministro cada producto. El sistema aborda esa trazabilidad con transacciones maestro-detalle y una unica fuente de datos.

## 3. Procesos clave y levantamiento

Los procesos modelados son abastecimiento, venta, mantenimiento del inventario, clientes/proveedores y administracion de personal/roles. La tecnica real utilizada para levantar informacion debe registrarse aqui: **[COMPLETAR: observacion, entrevista o revision documental, incluyendo fecha y responsable]**. El analisis tecnico adicional se realizo mediante auditoria del repositorio, entidades, configuracion, datos y ejecuciones Maven.

## 4. Problema y requerimientos

Problema: falta de una herramienta integrada que relacione entradas y salidas con el stock y los actores del negocio. Los requerimientos RF-01 a RF-08 se priorizan en `REQUERIMIENTOS.md`. Las compras deben incrementar stock exactamente una vez; las ventas deben validar el total solicitado por producto, impedir stock negativo y descontar exactamente una vez.

## 5. Objetivos

**Objetivo general:** implementar un sistema web verificable que centralice la gestion operativa del minimarket y mantenga la coherencia de su inventario.

**Objetivos especificos:**

- Modelar catalogos, personas, seguridad y transacciones con integridad referencial.
- Implementar CRUD y validaciones comprensibles.
- Automatizar entradas y salidas sin reproceso.
- Probar entidades y reglas con JUnit, Mockito y JaCoCo.
- Generar y verificar datos realistas, conservando 6,000 ventas donde el volumen lo justifica.
- Empaquetar un WAR compatible con Tomcat 9 y documentar su reproduccion.

## 6. Alcance y limitaciones

Incluye 13 entidades persistentes, dos detalles embebibles, CRUD OpenXava, inventario, carga academica y despliegue WAR. No incluye facturacion electronica SUNAT, contabilidad, pasarela bancaria, lotes fisicos ni integracion con lectores. `UsuarioSistema` es parte del dominio academico; la autenticacion visible usa NaviOX. La informacion real de la PYME y las capturas finales dependen del equipo.

## 7. Diseno de la solucion y arquitectura

La arquitectura separa presentacion OpenXava, acciones de aplicacion, dominio y persistencia. `InventarioServicio` depende de una interfaz y no de JPA directamente; `JpaInventarioRepositorio` actua como adaptador. Esta decision permite probar reglas con Mockito y conservar transacciones coordinadas por OpenXava.

La aplicacion de POO se evidencia en clases de entidad, embebibles por composicion, enumeraciones, interfaz de repositorio, implementacion polimorfica, encapsulamiento de reglas en metodos y excepcion de dominio.

## 8. Modelo E-R, 3FN e integridad

El diagrama `diagramas/modelo_er.mmd` refleja las relaciones reales. Cada entidad tiene una PK sustituta; las FK conectan productos, personal, seguridad y transacciones. Los detalles pertenecen a sus cabeceras mediante `@ElementCollection`, por decision obligatoria del proyecto.

El modelo evita repetir descripcion de categoria, marca, proveedor o cliente en transacciones. Los subtotales y totales se calculan desde cantidad y precio historico, por lo que no existe una columna derivada susceptible de quedar desactualizada. Las restricciones UNIQUE, NOT NULL, CHECK, validaciones Java y FK complementan la 3FN.

## 9. Diagramas de procesos y casos de uso

Los fuentes Mermaid de arquitectura, componentes, E-R, clases, casos de uso, compra, venta, inventario y autenticacion estan en `docs/diagramas/`. `CASOS_DE_USO.md` detalla actores, precondiciones, flujo normal y excepciones de los casos principales.

## 10. Tecnologias

- Java 25 y Lombok 1.18.40.
- OpenXava 7.7.3.
- JPA `javax.persistence`, Hibernate 5.6.15.Final.
- HSQLDB 2.7.4.
- Maven 3.9.15, WAR y Tomcat 9.0.118.
- JUnit 4.13.2, Mockito 5.12.0 y JaCoCo 0.8.14.

## 11. Implementacion y modulos

Los modulos son Cargo, CategoriaProducto, Cliente, Compra, Empleado, Marca, MetodoPago, Producto, Proveedor, Rol, UsuarioRol, UsuarioSistema y Venta. OpenXava proporciona listas, filtros, busqueda, detalle y CRUD. Compra y Venta agregan una accion `procesar` mediante controladores que heredan de `Typical`, preservando las acciones estandar.

Los montos usan `BigDecimal`. Las fechas usan `Date` por compatibilidad con la version actual. DNI, RUC, correo, telefono, username, password, cantidades, precios y stock poseen validaciones. El comprobante de factura requiere RUC. Los mensajes de negocio se centralizan en i18n.

## 12. Reglas de compra, venta e inventario

El servicio valida la coleccion completa antes de alterar productos. En venta agrupa cantidades por identidad de producto para impedir que dos lineas separadas eludan la disponibilidad. Solo despues descuenta y persiste. En compra suma mediante `Math.addExact`, evitando desbordamiento silencioso. `procesada` bloquea el doble movimiento.

La carga historica parte de 20 unidades y concilia cada producto como stock inicial mas compras menos ventas. Los precios quedan congelados en cada detalle para preservar el valor historico si luego cambia el catalogo.

## 13. CRUD, consultas y reportes

Todas las entidades generan CRUD mediante `Typical`. Las listas de OpenXava permiten filtros, orden, exportacion PDF/Excel y busqueda. Los reportes operativos sustentables incluyen bajo stock, ventas/compras por fecha, inventario valorizado y agrupaciones por cliente/proveedor; no se afirma una captura o reporte ejecutado si no consta en evidencias.

## 14. Pruebas JUnit, Mockito y JaCoCo

Se implementaron 21 metodos: 13 de entidades, 5 de inventario/repositorio con Mockito, 1 CRUD funcional y 2 de acciones. Las pruebas siguen JUnit 4 y `ModuleTestBase`, con login como primera instruccion. Mockito usa `when(...).thenReturn(...)`, excepciones simuladas, captor, `verify`, ausencia de interacciones ante error y no reproceso. JaCoCo exige 80 % de lineas del codigo propio y excluye lanzadores y utilidades de carga/verificacion.

La ejecucion autorizada por el usuario termino con 21 pruebas, cero fallos, cero errores y 82.48 % de cobertura de lineas.

## 15. Carga de datos realista

`CargaMasivaDatos` conserva 6,000 ventas, pero reduce configuraciones y maestros a cardinalidades defendibles: 8 cargos, 28 categorias, 120 marcas, 5 metodos, 6 roles, 3,000 clientes, 80 proveedores, 24 empleados, 1,800 productos, 12 usuarios, 14 asignaciones y 900 compras. Se generaron 7,730 detalles de compra y 24,037 de venta. Los 36 controles de calidad e integridad terminaron sin incidencias.

## 16. Compilacion, WAR y Tomcat

`mvn clean compile` y `mvn verify` terminaron con BUILD SUCCESS y produjeron el WAR. Tomcat 9 se selecciona por compatibilidad con `javax.*`; Tomcat 10/11 exigiria migracion Jakarta. Los scripts validan Java, Maven, CATALINA_HOME, WAR y respuesta HTTP.

## 17. Evidencias

Se conservan la salida de carga, CSV de conteos, verificacion de integridad y respaldo inicial. Las capturas que requieren interaccion visual se enumeran en `CAPTURAS_PENDIENTES.md`; no se fabrican imagenes ni resultados.

## 18. Analisis costo-beneficio

Sin datos financieros reales se presentan escenarios, no hechos. Variables editables: horas de registro evitadas al mes `H`, costo horario `C`, errores evitados `E` y perdida promedio por error `P`. Beneficio mensual estimado: `H*C + E*P`.

| Escenario | Supuesto ilustrativo | Beneficio esperado |
|---|---|---|
| Conservador | 10 h/mes y 2 errores evitados | Ahorro operativo limitado; validar con PYME |
| Esperado | 25 h/mes y 6 errores evitados | Menor digitacion y mejor reposicion |
| Favorable | 45 h/mes y 12 errores evitados | Mayor trazabilidad y disponibilidad |

Los costos a completar abarcan desarrollo, equipo, capacitacion, servidor, respaldo, mantenimiento y tiempo del personal. HSQLDB/OpenXava reducen licencias en el prototipo, pero produccion requiere evaluar soporte, seguridad y continuidad.

## 19. Indicadores de exito

- Tiempo promedio para registrar una venta y una compra.
- Porcentaje de transacciones procesadas sin correccion.
- Diferencia porcentual entre inventario fisico y sistema.
- Cantidad y duracion de productos agotados.
- Tiempo para generar un reporte mensual.
- Incidencias de stock negativo (meta: cero).
- Disponibilidad mensual y tiempo medio de recuperacion.
- Cobertura de lineas y tasa de pruebas exitosas.

Cada indicador necesita linea base, meta, responsable y periodicidad definidos con la PYME.

## 20. Riesgos

Los principales riesgos son credenciales de demostracion, copia inconsistente de archivos HSQLDB en ejecucion, falta de capturas/pruebas finales, datos reales incompletos de la PYME y uso de una base embebida fuera de su escala prevista. Las mitigaciones son cambio de clave, scripts con servidor detenido, checklist de evidencia, entrevista documentada y evaluacion de un motor servidor para produccion.

## 21. Conclusiones

El modelo corregido relaciona las operaciones centrales y elimina el uso de `double` en montos. Las reglas transaccionales impiden stock negativo y reproceso. La carga masiva y sus controles demuestran que el esquema soporta la exigencia academica. La separacion por capas hace que la logica critica sea comprobable independientemente de la interfaz.

La entrega tecnica alcanza un nivel alto en diseno, codigo, datos, pruebas y documentacion. El puntaje sobresaliente total depende de completar capturas institucionales y sustituir marcadores con informacion real de la PYME.

## 22. Recomendaciones

- Completar el levantamiento real firmado o fechado antes de presentar.
- Conservar junto a la entrega la evidencia Maven de 21 pruebas y JaCoCo 82.48 %.
- Realizar y capturar el flujo completo de compra/venta y reinicio de Tomcat.
- Cambiar `admin/admin` y usar hash robusto antes de un entorno no local.
- Evaluar PostgreSQL/MySQL y un modulo formal de movimientos/lotes si el prototipo evoluciona a produccion.

## 23. Referencias

- OpenXava 7.7, documentacion oficial.
- Jakarta/Javax Persistence 2.2 y Hibernate ORM 5.6.
- Apache Maven, JaCoCo, JUnit 4, Mockito y Apache Tomcat 9, documentacion oficial.
- `Lista de Cotejo - Proyecto Final de Curso`, proporcionada por el docente.
- `Rubrica de evaluacion para informe academico de proyecto final`, proporcionada por el docente.

## 24. Anexos

- A: matrices inicial y final.
- B: diccionario y esquema SQL.
- C: diagramas Mermaid.
- D: evidencias de carga e integridad.
- E: manuales y guias.
- F: lista de capturas pendientes.
