# Auditoria final

Fecha: 10 de julio de 2026.

| N. | Criterio oficial | Estado | Evidencia | Comando o archivo |
|---:|---|---|---|---|
| 1 | Compilacion, dependencias y Maven | Cumple | BUILD SUCCESS, WAR de 70 MB y SHA-256 | `mvn clean package -DskipTests -Djacoco.skip=true`, `maven_package.txt` |
| 2 | Interfaz intuitiva y completa | Cumple | Etiquetas espanolas, `@Tab`, `@View`, listas y filtros visualizados | capturas 01, 02, 03, 08, 10 y 11 |
| 3 | Validaciones | Cumple | Required, Pattern, Email, Size, Min, DecimalMin, AssertTrue y reglas de servicio | `modelo/`, `messages_es.properties` |
| 4 | CRUD completo | Cumple | `Typical` en todos los modulos y prueba CRUD ejecutada | `CategoriaProductoCrudTest`, `maven_verify_jacoco.txt` |
| 5 | Base de datos relacionada | Cumple | PK/FK/UNIQUE/indices, volumen realista y 36 controles OK | `validacion_final.txt`, scripts `verificar_*.sql` |
| 6 | JUnit y JaCoCo | Cumple | 21 pruebas OK y 82.48 % de lineas | `maven_verify_jacoco.txt`, `target/site/jacoco/` |
| 7 | Mockito | Cumple | mocks, captor, verify, no-interactions y acciones ejecutados | `InventarioServicioTest`, `AccionesTest` |
| 8 | Despliegue Tomcat | Cumple | Tomcat 9.0.118, HTTP 200, login/modulos, persistencia tras reinicio | `tomcat_deployment.txt`, `tomcat_smoke_test.txt`, capturas |
| 9 | Problema real de PYME | Cumple tecnicamente | compras, ventas, inventario, personas y seguridad integrados | falta completar datos reales de la PYME en el informe |
| 10 | Documentacion y capturas | Cumple | README, manuales, informe, diagramas y 6 capturas reales | `docs/`, `docs/evidencias/capturas/` |

## Evaluacion de la rubrica

| Dimension | Puntaje estimado | Evidencia | Brecha pendiente |
|---|---:|---|---|
| Diagnostico y requerimientos | 3/4 | Requerimientos priorizados, casos y diagnostico tecnico | Completar levantamiento real de la PYME |
| Diseno de la solucion | 4/4 | E-R en 3FN, PK/FK, arquitectura y nueve diagramas coherentes | Ninguna tecnica relevante |
| Codigo e implementacion | 4/4 | POO, capas, OpenXava, BigDecimal, interfaz/BD funcional | Ninguna tecnica relevante |
| Evidencias y despliegue | 4/4 | Capturas, WAR, HTTP 200, 21 pruebas y JaCoCo 82.48 % | Ninguna tecnica relevante |
| Costo-beneficio e indicadores | 4/4 | Tres escenarios, formula, indicadores, conclusiones y recomendaciones | Reemplazar supuestos si la PYME aporta cifras |

**Estimacion conservadora: 19/20.** La brecha restante corresponde al levantamiento institucional real, no a la implementacion tecnica.

## Incidencias corregidas durante la verificacion

1. Surefire anulaba `-DskipTests`; se elimino la configuracion fija.
2. La URL HSQLDB usaba un alias inexistente; se ajusto al alias vacio de `DBServer`.
3. Las colecciones con `@OrderColumn` eran `Collection`; se cambiaron a `List`, generando PK compuesta en detalles.
4. La interfaz mezclaba ingles y duplicaba referencias; se fijo espanol y `@Tab` explicitos.

## Pendientes reales

- Completar nombres de integrantes, docente, PYME y evidencia real del levantamiento.
- Capturar casos de validacion y mensajes de compra/venta si el docente exige todas las pantallas de la rubrica.
