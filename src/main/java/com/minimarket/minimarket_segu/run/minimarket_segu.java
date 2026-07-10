package com.minimarket.minimarket_segu.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicación.
 */

public class minimarket_segu {

	public static void main(String[] args) throws Exception {
		DBServer.start("minimarket_segu-db"); // Para usar tu propia base de datos comenta esta línea y configura src/main/webapp/META-INF/context.xml
		AppServer.run("minimarket_segu"); // Usa AppServer.run("") para funcionar en el contexto raíz
	}

}
