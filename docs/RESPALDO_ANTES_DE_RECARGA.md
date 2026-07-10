# Respaldo antes de la recarga realista

Fecha: 10 de julio de 2026. Motor: HSQLDB 2.7.4 en modo archivo/servidor.

El servidor OpenXava/HSQLDB fue detenido antes de copiar los archivos. Se ejecuto:

```bash
scripts/database/backup_database.sh backups/antes_de_recarga
```

Archivos verificados:

| Archivo | Tamano aproximado | SHA-256 |
|---|---:|---|
| minimarket_segu-db.script | 11 MB | `4bec3c65e461fb5de1f9139a87b86fb4c861d231e6d8a91cd3d08c7b55c45c3c` |
| minimarket_segu-db.properties | 110 B | `d54b639f0ce8ced5f8ca97fa441003984324a95be854ed7ca4cfd3989daf737f` |
| minimarket_segu-db.log | 35 B | `644ba2b8c34c7e892663ce64715ef6e59d83322bfe3d84fd77dcefc7394c5be9` |

El respaldo esta fuera de `target/` y conserva la carga anterior de 6,000 filas indiscriminadas. Para restaurarlo, con el servidor detenido:

```bash
scripts/database/restore_database.sh backups/antes_de_recarga
```
