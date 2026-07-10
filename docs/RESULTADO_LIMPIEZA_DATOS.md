# Resultado de limpieza de datos

La carga artificial anterior fue eliminada el 10 de julio de 2026 con el servidor detenido. Se limpiaron primero `DETALLE_VENTA`, `DETALLE_COMPRA` y `USUARIOROL`, luego transacciones y finalmente maestros/catalogos. Las secuencias se reiniciaron en 1.

No se elimino el esquema. El respaldo verificable anterior a la limpieza se encuentra en `backups/antes_de_recarga/` y se documenta en `RESPALDO_ANTES_DE_RECARGA.md`.

La ejecucion final esta registrada en `docs/evidencias/base-datos/resultado_recarga_final.txt`.
