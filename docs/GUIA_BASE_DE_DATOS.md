# Guia de base de datos

- Desarrollo/app: HSQLDB servidor en `jdbc:hsqldb:hsql://localhost:1666`. `DBServer` publica la base en el alias vacio por compatibilidad con el lanzador OpenXava.
- Mantenimiento/carga: HSQLDB archivo; detenga el servidor antes.
- Ver conteos: ejecute `VerificarBaseDatos` o `scripts/database/verify_counts.sql`.
- Ver integridad: use `verify_integrity.sql`.
- Respaldo/restauracion: scripts `.sh` o `.bat`; nunca copie los archivos mientras el puerto 1666 esta activo.
- Para reiniciar, conserve primero un respaldo y elimine los archivos `data/minimarket_segu-db.*`; Hibernate recreara el esquema al ejecutar el cargador.
