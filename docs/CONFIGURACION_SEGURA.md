# Configuracion segura

- La base HSQLDB y `admin/admin` son exclusivamente de demostracion local.
- Antes de publicar: cambie la cuenta administradora, restrinja el puerto 1666 a localhost y proteja los archivos `data/`.
- No se versionan claves reales, tokens ni rutas absolutas en codigo de produccion.
- No habilite el chat de IA sin proporcionar `OPENAI_API_KEY` mediante variable de entorno.
- `UsuarioSistema.password` representa el dominio academico y no sustituye la autenticacion NaviOX. Para produccion se requiere hash adaptativo (Argon2/bcrypt), sal individual y politica de rotacion.
- Tomcat debe ejecutarse con un usuario sin privilegios, TLS en el proxy, cookies seguras y permisos minimos.
- Los scripts de respaldo rechazan la copia mientras HSQLDB escucha en el puerto 1666, para evitar un respaldo inconsistente.
