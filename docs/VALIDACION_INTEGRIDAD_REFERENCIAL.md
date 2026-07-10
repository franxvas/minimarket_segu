# Validacion de integridad referencial

Los controles finales devolvieron cero para:

- empleados sin cargo;
- productos sin categoria o marca;
- usuarios sin empleado;
- asignaciones sin usuario o rol;
- compras sin proveedor o sin detalle;
- ventas sin cliente, empleado, metodo o detalle;
- detalles sin cabecera o producto.

Tambien se comprobo que todas las compras y ventas estan procesadas y que `stock_no_concilia = 0`. Evidencia: `docs/evidencias/base-datos/validacion_final.txt`.
