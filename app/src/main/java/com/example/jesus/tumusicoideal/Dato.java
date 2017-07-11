package com.example.jesus.tumusicoideal;

/**
 * Created by Jesus on 19/05/2017.
 * Nodo para enviar los datos al webservice
 */

public class Dato {
    String name, value;

    public Dato(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public String getValue()
    {
        return value;
    }
}
