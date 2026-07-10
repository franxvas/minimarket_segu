package com.minimarket.minimarket_segu.servicios;

/** Excepcion controlada para reglas del negocio del minimarket. */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String message) {
        super(message);
    }
}
