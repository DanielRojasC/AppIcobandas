package com.icobandas.icobandasapp.Entities;

public class Transportador {

    String idTransportador;
    String nombreTransportador;
    String idPlanta;
    String tipoTransportador;
    String caracteristicaTransportador;


    public String getIdTransportador() {
        return idTransportador;
    }

    public void setIdTransportador(String idTransportador) {
        this.idTransportador = idTransportador;
    }

    public String getNombreTransportador() {
        return nombreTransportador;
    }

    public void setNombreTransportador(String nombreTransportador) {
        this.nombreTransportador = nombreTransportador;
    }

    public String getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(String idPlanta) {
        this.idPlanta = idPlanta;
    }

    public String getTipoTransportador() {
        return tipoTransportador;
    }

    public void setTipoTransportador(String tipoTransportador) {
        this.tipoTransportador = tipoTransportador;
    }

    public String getCaracteristicaTransportador() {
        return caracteristicaTransportador;
    }

    public void setCaracteristicaTransportador(String caracteristicaTransportador) {
        this.caracteristicaTransportador = caracteristicaTransportador;
    }

    public Transportador(String idTransportador, String nombreTransportador, String idPlanta, String tipoTransportador, String caracteristicaTransportador) {
        this.idTransportador = idTransportador;
        this.nombreTransportador = nombreTransportador;
        this.idPlanta = idPlanta;
        this.tipoTransportador = tipoTransportador;
        this.caracteristicaTransportador = caracteristicaTransportador;
    }

    public Transportador() {
    }
}
