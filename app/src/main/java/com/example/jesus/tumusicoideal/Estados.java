package com.example.jesus.tumusicoideal;

/**
 * Created by Jesus on 18/05/2017.
 *  Nodo para obtener los estados del webservide
 *
 */

public class Estados {
    private int id;
    private String name;

    public Estados(){}

    public Estados(int id, String name){
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
