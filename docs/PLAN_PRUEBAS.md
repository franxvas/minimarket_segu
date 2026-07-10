# Plan de pruebas

| Grupo | Alcance | Casos clave | Evidencia esperada |
|---|---|---|---|
| Entidades | 13 entidades y 2 embebibles | construccion, relaciones, BigDecimal, totales, descuento, bajo stock | `EntidadesTest` |
| Inventario/Mockito | Servicio y repositorio | compra, venta, stock insuficiente, no reproceso, captor/no-interactions | `InventarioServicioTest` |
| CRUD | Interfaz OpenXava | crear, leer, actualizar y eliminar categoria | `CategoriaProductoCrudTest` |
| BD | 15 tablas de negocio | conteos, huerfanos, duplicados, valores invalidos | evidencias de BD |
| Despliegue | WAR/Tomcat | HTTP 200, login, consulta, persistencia tras reinicio | evidencias Tomcat |

Criterio de salida: cero fallos, cobertura de lineas >=80 %, integridad sin incidencias y smoke test HTTP exitoso.
