package com.example.jesus.tumusicoideal;

/**
 * Created by Jesus on 10/07/2017.
 */
public class Musico {

    String nombreCompleto;
    String telefono;

    Musico(String nombreCompleto, String telefono){
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
