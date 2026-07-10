# Guia de despliegue Tomcat

Se requiere Tomcat 9 porque OpenXava 7.7.3 usa APIs `javax.servlet`/`javax.persistence`. Tomcat 10 y 11 usan Jakarta y no son compatibles sin migracion.

```bash
export JAVA_HOME=/ruta/al/jdk-25
export CATALINA_HOME=/ruta/apache-tomcat-9
scripts/tomcat/deploy-tomcat.sh
scripts/tomcat/start-tomcat.sh
scripts/tomcat/smoke-test.sh
```

La aplicacion espera HSQLDB en el puerto 1666. Para un Tomcat externo, inicie antes el servidor de BD con el lanzador OpenXava o configure un servicio HSQLDB equivalente. Revise `$CATALINA_HOME/logs/catalina.out`, abra `http://localhost:8080/minimarket_segu/` y compruebe una consulta y una escritura. Reinicie y confirme persistencia antes de marcar el criterio como cumplido.
