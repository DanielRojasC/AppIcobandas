package com.icobandas.icobandasapp.Entities;

public class Clientes {

    private int nit;
    private String nombreCliente;

    public Clientes() {

    }

    public int getNit() {
        return nit;
    }

    public void setNit(int nit) {
        this.nit = nit;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Clientes(int nit, String nombreCliente) {
        this.nit = nit;
        this.nombreCliente = nombreCliente;
    }
}
