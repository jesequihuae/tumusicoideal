package com.example.jesus.tumusicoideal;

/**
 * Created by Jesus on 30/04/2017.
 */

public class Paises
{
    private int id;
    private String name;

    public Paises(){}

    public Paises(int id, String name){
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
