# Analisis de volumen de datos

Fecha: 10 de julio de 2026. Motor detectado: HSQLDB 2.7.4. El estado previo contiene 6,000 filas en cada entidad y 6,000 en cada tabla de detalle, producto de la carga academica mecanica anterior.

| Entidad | Tipo de entidad | Cantidad actual | Cantidad propuesta | Admite 6,000 | Justificacion |
|---|---|---:|---:|---|---|
| Cargo | Catalogo reducido | 6,000 | 8 | No | Un local requiere pocos puestos claramente diferenciados. El usuario solicito entre 4 y 8. |
| CategoriaProducto | Maestro/catalogo | 6,000 | 28 | No | Cubre familias comerciales sin categorias numeradas artificiales. |
| Marca | Maestro empresarial | 6,000 | 120 | No | Permite variedad nacional/importada plausible sin inflar el catalogo. |
| MetodoPago | Configuracion | 6,000 | 5 | No | Solo Efectivo, Yape, Plin, Tarjeta de debito y Tarjeta de credito. |
| Rol | Configuracion | 6,000 | 6 | No | Administrador, Gerente, Supervisor, Cajero, Almacenero y Compras. |
| Cliente | Persona/actor | 6,000 | 3,000 | Parcialmente | Es creible como cartera identificada de varios anos; correos Gmail opcionales. |
| Proveedor | Maestro empresarial | 6,000 | 80 | No | Cobertura suficiente para abastecimiento local y regional. |
| Empleado | Persona/actor | 6,000 | 24 | No | Plantilla razonable con turnos para un minimarket. |
| Producto | Maestro empresarial | 6,000 | 1,800 | Parcialmente | Variedad amplia por marca, sabor y presentacion sin llegar a un catalogo absurdo. |
| UsuarioSistema | Configuracion/actor | 6,000 | 12 | No | Solo empleados que necesitan acceso operativo. |
| UsuarioRol | Tabla intermedia | 6,000 | 14 | No | Una asignacion principal por usuario y dos asignaciones adicionales justificadas. |
| Compra | Transaccion | 6,000 | 900 | Si, pero no conviene | Las reposiciones son menos frecuentes que las ventas; 900 cubren 2024-2026. |
| DetalleCompra | Detalle transaccional | 6,000 | 5-12 por compra | Si | Cada reposicion incluye varios productos; el total final se obtiene tras cargar. |
| Venta | Transaccion | 6,000 | 6,000 | Si | Volumen historico razonable desde enero de 2024 hasta julio de 2026. |
| DetalleVenta | Detalle transaccional | 6,000 | 2-6 por venta | Si | Debe superar ampliamente a las ventas y representar canastas pequenas. |

## Relaciones y restricciones relevantes

- Las 13 entidades usan PK `ID`; los detalles embebidos usan PK compuesta `(cabecera_id, linea)`.
- `Empleado` requiere `Cargo`; `Producto` requiere categoria y marca; usuarios requieren empleado.
- `UsuarioRol` requiere usuario y rol y el par es unico.
- Compra/Venta requieren cabecera y detalles con producto, cantidad y precio positivos.
- DNI, RUC, correos no nulos, username, catalogos y comprobantes poseen restricciones de unicidad.
- El correo del cliente es opcional: se cargara Gmail unico o `NULL`, nunca una direccion real comprobable.

## Decision

Solo Venta conserva 6,000 cabeceras. Los detalles de venta superaran esa cifra por necesidad del modelo. Los catalogos y maestros se reducen a cantidades empresarialmente defendibles. Todos los nombres y datos personales seran sinteticos pero naturales; no se empleara informacion personal de terceros.
