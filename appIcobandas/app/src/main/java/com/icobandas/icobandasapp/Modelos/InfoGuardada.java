package com.icobandas.icobandasapp.Modelos;

public class InfoGuardada {

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNoLonas() {
        return noLonas;
    }

    public void setNoLonas(String noLonas) {
        this.noLonas = noLonas;
    }

    public String getTipoLonas() {
        return tipoLonas;
    }

    public void setTipoLonas(String tipoLonas) {
        this.tipoLonas = tipoLonas;
    }

    public String getAncho() {
        return ancho;
    }

    public void setAncho(String ancho) {
        this.ancho = ancho;
    }

    public String getEspesor() {
        return espesor;
    }

    public void setEspesor(String espesor) {
        this.espesor = espesor;
    }

    String marca;
    String noLonas;
    String tipoLonas;
    String ancho;
    String espesor;
    public InfoGuardada() {
    }
}
