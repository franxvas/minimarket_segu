# Plan de carga de datos realista

La regla anterior de 6,000 filas para cada tabla se descarto porque inflaba catalogos y configuraciones. La cardinalidad final depende del tipo de entidad y se justifica en `ANALISIS_VOLUMEN_DATOS.md`.

| Grupo | Entidades | Objetivo |
|---|---|---:|
| Configuracion | Cargo, MetodoPago, Rol | 8, 5 y 6 |
| Catalogos | CategoriaProducto, Marca, Producto | 28, 120 y 1,800 |
| Actores | Cliente, Proveedor, Empleado | 3,000, 80 y 24 |
| Seguridad | UsuarioSistema, UsuarioRol | 12 y 14 |
| Transacciones | Compra, Venta | 900 y 6,000 |
| Detalles | DetalleCompra, DetalleVenta | 7,730 y 24,037 |

La carga usa semilla fija, lotes de 100, reinicio controlado e IDs referenciales existentes. Solo `Venta` conserva 6,000 cabeceras porque representa un historial razonable; los detalles son mayores porque cada transaccion contiene varias lineas.
