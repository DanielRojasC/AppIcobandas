package com.icobandas.icobandasapp.Entities;

public class BandaPesada {

    public String getTipoParteTransmision() {
        return tipoParteTransmision;
    }

    public void setTipoParteTransmision(String tipoParteTransmision) {
        this.tipoParteTransmision = tipoParteTransmision;
    }

    public String getAnchoBandaTransmision() {
        return anchoBandaTransmision;
    }

    public void setAnchoBandaTransmision(String anchoBandaTransmision) {
        this.anchoBandaTransmision = anchoBandaTransmision;
    }

    public String getDistanciaEntreCentrosTransmision() {
        return distanciaEntreCentrosTransmision;
    }

    public void setDistanciaEntreCentrosTransmision(String distanciaEntreCentrosTransmision) {
        this.distanciaEntreCentrosTransmision = distanciaEntreCentrosTransmision;
    }

    public String getPotenciaMotorTransmision() {
        return potenciaMotorTransmision;
    }

    public void setPotenciaMotorTransmision(String potenciaMotorTransmision) {
        this.potenciaMotorTransmision = potenciaMotorTransmision;
    }

    public String getRpmSalidaReductorTransmision() {
        return rpmSalidaReductorTransmision;
    }

    public void setRpmSalidaReductorTransmision(String rpmSalidaReductorTransmision) {
        this.rpmSalidaReductorTransmision = rpmSalidaReductorTransmision;
    }

    public String getDiametroPoleaConducidaTransmision() {
        return diametroPoleaConducidaTransmision;
    }

    public void setDiametroPoleaConducidaTransmision(String diametroPoleaConducidaTransmision) {
        this.diametroPoleaConducidaTransmision = diametroPoleaConducidaTransmision;
    }

    public String getAnchoPoleaConducidaTransmision() {
        return anchoPoleaConducidaTransmision;
    }

    public void setAnchoPoleaConducidaTransmision(String anchoPoleaConducidaTransmision) {
        this.anchoPoleaConducidaTransmision = anchoPoleaConducidaTransmision;
    }

    public String getDiametroPoleaMotrizTransmision() {
        return diametroPoleaMotrizTransmision;
    }

    public void setDiametroPoleaMotrizTransmision(String diametroPoleaMotrizTransmision) {
        this.diametroPoleaMotrizTransmision = diametroPoleaMotrizTransmision;
    }

    public String getAnchoPoleaMotrizTransmision() {
        return anchoPoleaMotrizTransmision;
    }

    public void setAnchoPoleaMotrizTransmision(String anchoPoleaMotrizTransmision) {
        this.anchoPoleaMotrizTransmision = anchoPoleaMotrizTransmision;
    }

    String tipoParteTransmision;
    String anchoBandaTransmision;
    String distanciaEntreCentrosTransmision;
    String potenciaMotorTransmision;
    String rpmSalidaReductorTransmision;
    String diametroPoleaConducidaTransmision;
    String anchoPoleaConducidaTransmision;
    String diametroPoleaMotrizTransmision;
    String anchoPoleaMotrizTransmision;

    public String getObservacionRegistro() {
        return observacionRegistro;
    }

    public void setObservacionRegistro(String observacionRegistro) {
        this.observacionRegistro = observacionRegistro;
    }

    String observacionRegistro;
    String idRegistro;

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public BandaPesada() {
    }
}
