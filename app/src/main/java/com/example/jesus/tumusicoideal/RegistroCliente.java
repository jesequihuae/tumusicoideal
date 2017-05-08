package com.example.jesus.tumusicoideal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RegistroCliente extends AppCompatActivity implements View.OnClickListener{

    private static final int PHOTO_SELECTED = 1;

    Spinner spinnerPais;
    MaterialBetterSpinner spinnerEstado;
    ArrayAdapter<Paises> arrayAdapterPais;
    ArrayAdapter<String> arrayAdapterEstado;
    Button btnGaleria;
    ImageView imagePreview;
    private Uri url_imagen;
    Toolbar toolbar;
    public ArrayList<Paises> PaisDatos = new ArrayList<Paises>();
    String[] EstadoDatos = {"Michoacan", "Aguascalientes"};
    String[] CiudadDatos = {"Apatzingan", "Aguascalientes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.registroUsuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new GetPaises().execute();

        arrayAdapterPais = new ArrayAdapter<Paises>(this,android.R.layout.simple_dropdown_item_1line, PaisDatos);
        spinnerPais =  (Spinner) findViewById(R.id.spinnerPais);
        spinnerPais.setAdapter(arrayAdapterPais);
        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RegistroCliente.this,
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        arrayAdapterEstado = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, EstadoDatos);
        spinnerEstado = (MaterialBetterSpinner) findViewById(R.id.spinnerEstado);
        spinnerEstado.setAdapter(arrayAdapterEstado);
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("****ASASASA***","si ENTRO");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //llenarPaises(); //Llnea lista de paises

       // initSpinners(); //Inicializa los Spinners
        btnGaleria = (Button) findViewById(R.id.btn_abrirGaleria);
        btnGaleria.setOnClickListener(this);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

    }

    protected  void initSpinners(){

        ArrayAdapter<Paises> arrayAdapterPais = new ArrayAdapter<Paises>(this,android.R.layout.simple_dropdown_item_1line, PaisDatos);
        ArrayAdapter<String> arrayAdapterEstado = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, EstadoDatos);
        ArrayAdapter<String> arrayAdapterCiudad = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CiudadDatos);

        spinnerPais =  (Spinner) findViewById(R.id.spinnerPais);
        MaterialBetterSpinner spinnerEstado = (MaterialBetterSpinner) findViewById(R.id.spinnerEstado);
        MaterialBetterSpinner spinnerCiudad = (MaterialBetterSpinner) findViewById(R.id.spinnerCiudad);

        spinnerPais.setAdapter(arrayAdapterPais);
        spinnerEstado.setAdapter(arrayAdapterEstado);
        spinnerCiudad.setAdapter(arrayAdapterCiudad);

        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_abrirGaleria){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent, "Seleccione una imagen"),
                    PHOTO_SELECTED);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("******** IMAGEN *******","si entró");
        if (requestCode == PHOTO_SELECTED) {

            if(data == null){ return; }
            url_imagen = data.getData();
            try {
                mostrarPreview();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public void mostrarPreview() throws FileNotFoundException {
        if(url_imagen.toString().equalsIgnoreCase("NO"))
            return;
        InputStream stream = RegistroCliente.this.getContentResolver().openInputStream(url_imagen);
        BufferedInputStream buffer = new BufferedInputStream(stream);
        Bitmap imagen = BitmapFactory.decodeStream(buffer);
        imagePreview.setImageBitmap(imagen);
    }


    private class GetPaises extends AsyncTask<Void, Void, Void>
    {

        protected void onPostExecute(){

        }

        @Override
        protected Void doInBackground(Void... params) {
            String sql = "http://tumusicoideal.tatallerarquitectura.com/ubicacion/paises";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = null;
            HttpURLConnection conn;

            try {
                url = new URL(sql);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                int con = conn.getResponseCode();
                if(con == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    String json = "";

                    while((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }

                    json = response.toString();

                    JSONArray jsonAr = null;

                    try {
                        jsonAr = new JSONArray(json);
                        for (int i = 0; i < jsonAr.length(); i++)
                        {
                            JSONObject obj = (JSONObject) jsonAr.get(i);
                            Paises pais = new Paises(obj.getInt("id"), obj.getString("pais"));
                            Log.d("**TAG***", pais.getId()+"");
                            PaisDatos.add(pais);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}


