package com.appshop.jesus.appshop.carrito.models;

public enum EstadoCarrito {
    ACTIVO("ACTIVO"),
    COMPRADO("COMPRADO"),
    CANCELADO("CANCELADO");

    private final String estado;

    EstadoCarrito(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public static EstadoCarrito fromString(String estado) {
        if (estado != null) {
            for (EstadoCarrito estadoCarrito : EstadoCarrito.values()) {
                if (estado.equalsIgnoreCase(estadoCarrito.getEstado())) {
                    return estadoCarrito;
                }
            }
        }
        return null; // O puedes lanzar una excepción si prefieres
    }
}
