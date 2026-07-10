# Diccionario de datos

| Tabla | Proposito | PK/FK principales | Restricciones destacadas |
|---|---|---|---|
| CARGO | Puestos y sueldo base | ID | nombre unico, sueldo no negativo |
| CATEGORIAPRODUCTO | Clasificacion de productos | ID | nombre unico |
| MARCA | Marca comercial | ID | nombre unico |
| METODOPAGO | Medios aceptados | ID | nombre unico |
| CLIENTE | Datos de compradores | ID | DNI/RUC/correo unicos y con formato |
| PROVEEDOR | Abastecedores | ID | RUC/correo unicos |
| EMPLEADO | Personal | ID; CARGO_ID | DNI/correo unicos |
| PRODUCTO | Precios y existencias | ID; CATEGORIA_ID, MARCA_ID | montos y stock no negativos |
| USUARIOSISTEMA | Cuenta de dominio | ID; EMPLEADO_ID | username unico |
| ROL | Perfil del sistema | ID | nombre unico |
| USUARIOROL | Asignacion de perfil | ID; USUARIO_ID, ROL_ID | par usuario-rol unico |
| COMPRA | Cabecera de abastecimiento | ID; PROVEEDOR_ID | comprobante unico, procesada |
| DETALLE_COMPRA | Lineas embebidas | COMPRA_ID, PRODUCTO_ID | cantidad/precio positivos |
| VENTA | Cabecera de salida | ID; CLIENTE_ID, EMPLEADO_ID, METODOPAGO_ID | comprobante unico, procesada |
| DETALLE_VENTA | Lineas embebidas | VENTA_ID, PRODUCTO_ID | cantidad/precio positivos |
