package com.example.jesus.tumusicoideal;

/**
 * Created by Jesus on 10/07/2017.
 */
public class Musico {

    String nombreCompleto;
    String telefono;
    String idMusico;

    Musico(String id, String nombreCompleto, String telefono){
        this.idMusico = id;
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

    public String getIdMusico() {
        return idMusico;
    }

    public void setIdMusico(String idMusico) {
        this.idMusico = idMusico;
    }
}
