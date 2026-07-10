# Guia de pruebas

El repositorio exige JUnit 4, `ModuleTestBase` y la primera linea `login("admin", "admin")`. Para esta entrega el usuario autorizo expresamente su ejecucion con Maven.

1. Inicie la aplicacion en el puerto 8080.
2. Sin ejecutar `clean` mientras el servidor esta activo, ejecute `mvn verify`.
3. Verifique 21 metodos, cero fallos y al menos 80 % de lineas del codigo propio.
4. Abra `target/site/jacoco/index.html` para consultar el informe.

Resultado del 10 de julio de 2026: `BUILD SUCCESS`, 21/21 pruebas y 82.48 % de lineas.
