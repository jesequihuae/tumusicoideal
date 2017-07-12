package com.example.jesus.tumusicoideal;

/**
 * Created by Jesus on 11/07/2017.
 */

public class Horario_data {

    String hora_inicial;
    String hora_final;
    String dia;

    Horario_data(String hora_inicial, String hora_final, int dia)
    {
        this.hora_inicial = hora_inicial;
        this.hora_final = hora_final;
        this.dia = diaTexto(dia);
    }

    public String diaTexto(int diaNumero)
    {
        String dia = "";

        switch (diaNumero){
            case 1:
                dia = "Lunes";
                break;
            case 2:
                dia = "Martes";
                break;
            case 3:
                dia = "Miercoles";
                break;
            case 4:
                dia = "Jueves";
                break;
            case 5:
                dia = "Viernes";
                break;
            case 6:
                dia = "Sabado";
                break;
            case 7:
                dia = "Domingo";
                break;
        }

        return dia;
    }

    public String getHora_inicial() {
        return hora_inicial;
    }

    public String getHora_final() {
        return hora_final;
    }

    public String getDia() {
        return dia;
    }
}
