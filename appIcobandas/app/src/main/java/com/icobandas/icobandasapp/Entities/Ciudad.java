package com.icobandas.icobandasapp.Entities;

public class Ciudad {

    String codCiudad;
    String nombreCiudad;

    public String getCodCiudad() {
        return codCiudad;
    }

    public void setCodCiudad(String codCiudad) {
        this.codCiudad = codCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public Ciudad(String codCiudad, String nombreCiudad) {
        this.codCiudad = codCiudad;
        this.nombreCiudad = nombreCiudad;
    }
}
