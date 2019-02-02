package com.icobandas.icobandasapp.Entities;

public class Usuarios {



    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getContraseñaUsuario() {
        return contraseñaUsuario;
    }

    public void setContraseñaUsuario(String contraseñaUsuario) {
        this.contraseñaUsuario = contraseñaUsuario;
    }
    private String nombreUsuario;
    private String nombreVendedor;
    private String contraseñaUsuario;

    public Usuarios(String nombreUsuario, String nombreVendedor, String contraseñaUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.nombreVendedor = nombreVendedor;
        this.contraseñaUsuario = contraseñaUsuario;
    }
}
