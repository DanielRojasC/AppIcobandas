package com.icobandas.icobandasapp.Entities;

public class Registro {

    String idRegistro;
    String fechaRegistro;
    String idTransportador;
    String codPlanta;
    String usuarioRegistro;

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdTransportador() {
        return idTransportador;
    }

    public void setIdTransportador(String idTransportador) {
        this.idTransportador = idTransportador;
    }

    public String getCodPlanta() {
        return codPlanta;
    }

    public void setCodPlanta(String codPlanta) {
        this.codPlanta = codPlanta;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Registro() {
    }
}
