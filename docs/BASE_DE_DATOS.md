# Base de datos

## Plataforma

- Motor: HSQLDB 2.7.4, modo archivo para mantenimiento y modo servidor (`jdbc:hsqldb:hsql://localhost:1666`, alias vacio) al ejecutar la aplicacion.
- Base: `minimarket_segu`.
- Usuario local de desarrollo: `sa`, sin clave. No usar esta configuracion fuera de un equipo de demostracion.
- ORM: Hibernate 5.6.15.Final / JPA `javax.persistence`.

## Modelo

Las 13 tablas de entidad poseen PK `ID`. Las tablas detalle dependen de sus cabeceras por FK, existen por `@ElementCollection` y usan PK compuesta `(cabecera_id, linea)` generada por `List` + `@OrderColumn`; no son entidades independientes. Las FK cubren cargo-empleado, categoria/marca-producto, empleado-usuario, usuario-rol, proveedor-compra, cliente/empleado/metodo-venta y producto en ambos detalles.

Se aplican restricciones `UNIQUE` a DNI, RUC, correo, username, nombres de catalogos y numeros de comprobante; `CHECK` y validaciones del modelo evitan stock, cantidades y precios negativos. Los indices principales optimizan nombre, fecha, stock y FK de consulta.

El modelo alcanza 3FN en las entidades: catalogos, personas, seguridad y transacciones se separan; los detalles dependen de su cabecera y producto. Los totales/subtotales son calculados, no duplicados en columnas, evitando inconsistencias derivadas.

## Creacion, respaldo y restauracion

- DDL: `scripts/database/create_database.sql` y `schema.sql`.
- Conteos/integridad: `verify_counts.sql` y `verify_integrity.sql`.
- Respaldo: detener HSQLDB y ejecutar `scripts/database/backup_database.sh`.
- Restauracion: detener HSQLDB y ejecutar `scripts/database/restore_database.sh RUTA`.

El respaldo previo a la limpieza se conserva en `docs/evidencias/base-datos/respaldo_inicial/`.
