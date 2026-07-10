# Casos de uso

## Actores

- Administrador: configura catalogos, empleados, usuarios y consulta todos los modulos.
- Cajero: registra clientes y ventas.
- Encargado de almacen/compras: mantiene productos, proveedores y compras.

## Casos principales

### CU-01 Registrar compra

Precondicion: proveedor y producto existentes. El usuario crea la compra, agrega uno o mas detalles y guarda. Luego pulsa **Procesar**. El sistema valida cantidades/precios, incrementa stock y marca la compra. Si ya fue procesada, no repite el movimiento.

### CU-02 Registrar venta

Precondicion: cliente, empleado, metodo y productos existentes. El usuario agrega detalles y guarda. Al procesar, el sistema agrupa cantidades por producto, verifica el stock total, descuenta y marca la venta. Si cualquier producto es insuficiente, no modifica ninguno.

### CU-03 Consultar bajo stock

El usuario abre Productos y filtra por `bajoStock=true` o compara `stock` con `stockMinimo`.

### CU-04 Administrar maestros

El usuario autorizado crea, consulta, modifica y elimina catalogos mediante las acciones `Typical`, sujeto a las FK existentes.
