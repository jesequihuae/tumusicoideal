package com.example.jesus.tumusicoideal;

import java.util.List;

/**
 * Created by Jesus on 11/07/2017.
 */

public class MusicoInformacion_data
{
    String nombreCompleto;
    String telefonoCelular;
    String telefonoCasa;
    String disponibilidad;

    public MusicoInformacion_data(String nombreCompleto, String telefonoCelular, String telefonoCasa, String disponibilidad)
    {
        this.nombreCompleto = nombreCompleto;
        this.telefonoCelular = telefonoCelular;
        this.telefonoCasa = telefonoCasa;
        this.disponibilidad = disponibilidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getTelefonoCasa() {
        return telefonoCasa;
    }

    public void setTelefonoCasa(String telefonoCasa) {
        this.telefonoCasa = telefonoCasa;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

}
