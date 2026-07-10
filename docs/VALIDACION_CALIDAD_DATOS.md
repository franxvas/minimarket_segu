# Validacion de calidad de datos

Resultado final: todos los controles en estado `OK`.

- Cero duplicados normalizados en catalogos, productos, DNI, RUC, correos, usuarios y asignaciones usuario-rol.
- Cero campos obligatorios faltantes en clientes, proveedores y productos.
- Cero correos distintos de Gmail en clientes, empleados o proveedores; 1,051 clientes tienen correo nulo de forma valida.
- Cero nombres residuales `Cliente demo`, `Producto demo`, `Proveedor demo` o `carga academica`.
- Precios positivos y margen no negativo.
- Cinco metodos exactos: Efectivo, Yape, Plin, Tarjeta de debito y Tarjeta de credito.

Muestra de clientes:

| Nombre | DNI ficticio | Correo |
|---|---|---|
| Publico General | 00000000 | NULL |
| Maria Fernanda Mendoza Salazar | 70000001 | mmendoza.salazar@gmail.com |
| Jose Carlos Mendoza Salazar | 70000002 | NULL |
| Andrea Lucia Mendoza Salazar | 70000003 | andrea.mendoza@gmail.com |
| Rosa Elena Mendoza Salazar | 70000004 | rmendoza.salazar@gmail.com |

Los DNI, RUC, nombres de personas y empresas son datos sinteticos de prueba.
