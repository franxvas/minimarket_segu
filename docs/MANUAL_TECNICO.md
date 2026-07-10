# Manual tecnico

El proyecto es un WAR Maven de un solo modulo. OpenXava inspecciona las entidades de `modelo`, crea modulos CRUD y usa `controladores.xml` para acciones propias. El acceso a datos usa JPA de campo y la persistencia se declara en `persistence.xml`.

`InventarioServicio` valida todos los detalles antes de mutar productos; asi una venta con una linea insuficiente no deja cambios parciales. `JpaInventarioRepositorio` adapta el servicio al contexto JPA. Los precios congelados en detalles preservan el importe historico.

La carga usa la unidad `carga` en modo archivo y debe ejecutarse con el servidor detenido. La unidad `default` usa JNDI en Tomcat. La unidad `junit` es HSQLDB en memoria.

No modifique entidades para registrar acciones: hagalo en `src/main/resources/xava/controladores.xml`, heredando `Typical`.
