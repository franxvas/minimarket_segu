# Plan de recarga realista

1. Detener OpenXava/HSQLDB para evitar escrituras concurrentes.
2. Respaldar `data/minimarket_segu-db.*` y comprobar SHA-256.
3. Exigir argumento `--reset-and-seed` y variable `MINIMARKET_SEED_CONFIRM=RESET_REALISTIC_DATA`.
4. Borrar detalles, transacciones, relaciones, maestros y catalogos en orden seguro.
5. Reiniciar identidades, cargar catalogos, actores, productos, seguridad, compras y ventas.
6. Conciliar `stock = 20 + compras - ventas` por producto.
7. Ejecutar `VerificarBaseDatos` y conservar su salida como evidencia.

La semilla fija `20260710` hace que los resultados y conteos sean repetibles.
