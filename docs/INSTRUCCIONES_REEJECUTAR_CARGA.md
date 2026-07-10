# Instrucciones para reejecutar la carga

Requisitos: Java 25, Maven 3.9 y servidor OpenXava/HSQLDB detenido.

```bash
scripts/database/backup_database.sh backups/antes_de_otra_recarga
mvn clean compile
MINIMARKET_SEED_CONFIRM=RESET_REALISTIC_DATA mvn exec:java \
  -Dexec.mainClass=com.minimarket.minimarket_segu.datos.CargaMasivaDatos \
  -Dexec.classpathScope=runtime \
  -Dexec.args=--reset-and-seed
mvn exec:java \
  -Dexec.mainClass=com.minimarket.minimarket_segu.datos.VerificarBaseDatos \
  -Dexec.classpathScope=runtime
```

Sin las dos confirmaciones la clase cancela la operacion antes de abrir la unidad de persistencia. La recarga borra los datos actuales, por eso el respaldo es obligatorio en un entorno que no sea descartable.
