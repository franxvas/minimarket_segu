# Requerimientos

## Contexto pendiente de validar

PYME: `[COMPLETAR CON INFORMACION DE LA PYME]`. El diagnostico tecnico parte del problema observable del dominio: registros dispersos dificultan conocer stock, compras, ventas, clientes y proveedores en tiempo oportuno.

## Funcionales priorizados

| ID | Prioridad | Requerimiento | Justificacion |
|---|---|---|---|
| RF-01 | Alta | Mantener productos, categorias y marcas | Base del inventario y transacciones |
| RF-02 | Alta | Registrar compras con detalles e incrementar stock una vez | Evita inventario subestimado y reproceso |
| RF-03 | Alta | Registrar ventas con detalles, validar disponibilidad y descontar stock una vez | Evita venta sin existencias |
| RF-04 | Alta | Mantener clientes y proveedores con DNI/RUC/correo validos | Mejora trazabilidad comercial |
| RF-05 | Alta | Administrar empleados, usuarios y roles | Identifica responsables y perfiles |
| RF-06 | Media | Consultar bajo stock mediante stock minimo | Facilita reposicion |
| RF-07 | Media | Filtrar listas y exportar reportes de OpenXava | Apoya control y presentacion |
| RF-08 | Alta | Cargar datos realistas de forma repetible y mantener 6,000 ventas historicas | Evita catalogos artificiales sin perder volumen transaccional |

## No funcionales

- RNF-01: compilar reproduciblemente con Maven y JDK 25.
- RNF-02: funcionar en Tomcat 9 y HSQLDB 2.7.4.
- RNF-03: mantener integridad con PK, FK, UNIQUE y validaciones.
- RNF-04: procesar carga masiva en lotes sin ejecutarla al iniciar.
- RNF-05: presentar mensajes en espanol y una interfaz navegable.
- RNF-06: conservar evidencia reproducible de build, datos, pruebas y despliegue.
