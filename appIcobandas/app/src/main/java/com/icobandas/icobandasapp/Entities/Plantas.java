package com.icobandas.icobandasapp.Entities;

public class Plantas {

    String idPlanta;
    String nombrePlanta;
    String nitCliente;
    String idCiudad;
    String direccionPlanta;

    public Plantas() {

    }

    public String getNombrePlanta() {
        return nombrePlanta;
    }

    public void setNombrePlanta(String nombrePlanta) {
        this.nombrePlanta = nombrePlanta;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public String getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getDireccionPlanta() {
        return direccionPlanta;
    }

    public void setDireccionPlanta(String direccionPlanta) {
        this.direccionPlanta = direccionPlanta;
    }

    public String getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(String idPlanta) {
        this.idPlanta = idPlanta;
    }

    public Plantas(String nombrePlanta, String nitCliente, String idCiudad, String direccionPlanta, String idPlanta) {
        this.nombrePlanta = nombrePlanta;
        this.nitCliente = nitCliente;
        this.idCiudad = idCiudad;
        this.direccionPlanta = direccionPlanta;
        this.idPlanta = idPlanta;
    }


}
