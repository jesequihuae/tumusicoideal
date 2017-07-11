package com.example.jesus.tumusicoideal;

/**
 * Created by Jesus on 18/05/2017.
 *
 * Nodo para obtener las ciudades de la base de datos.
 */

public class Ciudades {
    private int id;
    private String name;

    public Ciudades(){}

    public Ciudades(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public String toString()
    {
        return this.name;
    }
}
