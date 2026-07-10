# Matriz oficial de cumplimiento

Fuente: `Lista de cotejo.pdf` y `Rubrica de Informe Academico.pdf`, entregadas por el usuario el 10 de julio de 2026.

## Lista de cotejo (10 criterios)

| N. | Criterio oficial resumido | Estado actual | Evidencia | Brecha/evidencia final esperada |
|---:|---|---|---|---|
| 1 | Compila sin errores ni conflictos Maven | Cumple | `mvn clean compile` BUILD SUCCESS | Adjuntar salida final de package |
| 2 | Interfaz intuitiva y sin campos faltantes | Cumple | `@View`, `@Tab`, etiquetas espanolas y seis capturas reales | Ampliar capturas si el docente pide cada CRUD |
| 3 | Formularios con validaciones | Cumple | Anotaciones en `modelo/`, mensajes i18n | Capturas de errores de validacion |
| 4 | CRUD completo | Cumple | Controlador `Typical` y `CategoriaProductoCrudTest` ejecutado | Ninguna tecnica relevante |
| 5 | BD con tablas relacionadas, PK y FK | Cumple | `data/`, `schema.sql`, verificacion sin huerfanos | Evidencia visual del esquema |
| 6 | JUnit con JaCoCo en entidades | Cumple | 21 pruebas y JaCoCo 82.48 % de lineas | Reporte en `target/site/jacoco/` |
| 7 | Mockito completo | Cumple | `InventarioServicioTest` y `AccionesTest`, todos OK | Ninguna tecnica relevante |
| 8 | Despliegue Tomcat funcional | Cumple | Tomcat 9.0.118, HTTP 200, login, modulos y persistencia tras reinicio | Ninguna tecnica relevante |
| 9 | Resuelve problema real de PYME | Cumple | Productos, stock, compras, ventas, clientes, proveedores, usuarios | Completar datos reales de la PYME |
| 10 | Manuales y capturas | Cumple | README, manuales, informe, diagramas y seis capturas reales | Agregar validaciones/pruebas al ejecutar desde IDE |

## Rubrica del informe (maximo 20)

| Dimension oficial | Meta sobresaliente (4) | Evidencia actual | Estimacion conservadora | Brecha |
|---|---|---|---:|---|
| Diagnostico y levantamiento | Procesos completos, requerimientos priorizados y justificados por diagnostico real | `REQUERIMIENTOS.md`, `CASOS_DE_USO.md`, informe con marcadores | 3/4 | Completar informacion real de la PYME y tecnica de levantamiento |
| Diseno de la solucion | E-R en 3FN, integridad, arquitectura clara y procesos coherentes | `ARQUITECTURA.md`, `BASE_DE_DATOS.md`, Mermaid | 4/4 | Renderizar diagramas si el docente exige imagen |
| Codigo e implementacion | Codigo limpio, POO, GUI intuitiva, BD estable y modulos justificados | Modelo, servicios, acciones, carga y compilacion | 4/4 | Validacion visual completa |
| Evidencias y despliegue | Capturas CRUD/consultas/eventos/reportes, validaciones, JUnit/JaCoCo/Mockito y ejecutable | WAR, HTTP 200, capturas, 21 pruebas y JaCoCo 82.48 % | 4/4 | Ninguna tecnica relevante |
| Costo-beneficio e indicadores | Analisis cuantitativo/cualitativo, costos realistas, beneficios medibles, conclusiones | Informe con tres escenarios e indicadores | 4/4 | Sustituir supuestos con datos de la PYME si estan disponibles |

Estimacion actual: **19/20**. No se declara 20/20 porque falta completar el levantamiento real de la PYME.
