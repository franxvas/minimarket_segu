# Manual de instalacion

1. Instale JDK 25, Maven 3.9.x y Git. Para despliegue externo, instale Tomcat 9.0.x.
2. Clone/copíe el repositorio y defina `JAVA_HOME` al JDK 25.
3. Confirme con `mvn -version` que Maven usa Java 25.
4. Ejecute `mvn clean compile`.
5. Si reconstruye datos, detenga la aplicacion y ejecute el cargador descrito en README.
6. Genere el WAR con `mvn package -DskipTests -Djacoco.skip=true`.
7. Para desarrollo, ejecute `mvn exec:java`; para Tomcat externo use `scripts/tomcat/deploy-tomcat.sh`.

Problemas frecuentes: el puerto 1666 ocupado indica otra instancia HSQLDB; el 8080 ocupado indica otro Tomcat. La mezcla Tomcat 10/11 con `javax.*` produce errores y no esta soportada.
