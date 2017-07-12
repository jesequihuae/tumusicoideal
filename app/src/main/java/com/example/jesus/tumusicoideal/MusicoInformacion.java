package com.example.jesus.tumusicoideal;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MusicoInformacion extends AppCompatActivity {

    public final String SERVER = "https://jesusequihua.000webhostapp.com/tumusicoapi";
    String idMusico;
    List<MusicoInformacion_data> informacion = new ArrayList<>();
    List<String> Instrumentos = new ArrayList<>();
    List<String> Excepciones = new ArrayList<>();
    List<Horario_data> Horarios = new ArrayList<>();

    Toolbar toolbar;
    ProgressDialog progress;

    private RecyclerView instrumentosRecycler;
    private RecyclerView horariosRecycler;
    LinearLayoutManager lManager, lManager2;

    //Datos musico
    TextView nombreCompleto;
    TextView telefonoCelular;
    TextView telefonoCasa;
    TextView disponibilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musico_informacion);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        idMusico = getIntent().getExtras().getString("id");

        toolbar = (Toolbar) findViewById(R.id.toolbarInformacion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.informacionUsuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RecyclerView
        instrumentosRecycler = (RecyclerView) findViewById(R.id.instrumentoRecycler);
        lManager = new LinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        instrumentosRecycler.setLayoutManager(lManager);

        horariosRecycler = (RecyclerView) findViewById(R.id.horariosRecycler);
        lManager2 = new LinearLayoutManager(this);
        lManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        horariosRecycler.setLayoutManager(lManager2);

        new getInformacion().execute();

    }

    public void mostrarInformacion()
    {
        nombreCompleto = (TextView) findViewById(R.id.nombre);
        telefonoCelular = (TextView) findViewById(R.id.telefonoCelular);
        telefonoCasa = (TextView) findViewById(R.id.telefonoCasa);
        disponibilidad = (TextView) findViewById(R.id.disponible);

        nombreCompleto.setText(informacion.get(0).getNombreCompleto());
        telefonoCelular.setText("Cel: "+informacion.get(0).getTelefonoCelular());
        telefonoCasa.setText("Tel: "+informacion.get(0).getTelefonoCasa());

        if( informacion.get(0).getDisponibilidad().equals("1") ) {
            disponibilidad.setText("Disponible");
            disponibilidad.setTextColor(Color.GREEN);
        } else {
            disponibilidad.setText("No disponible");
            disponibilidad.setTextColor(Color.RED);
        }

        RVInstrumentosAdapter adapter = new RVInstrumentosAdapter(Instrumentos);
        instrumentosRecycler.setAdapter(adapter);

        RVHorariosAdapter adapter1 = new RVHorariosAdapter(Horarios);
        horariosRecycler.setAdapter(adapter1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class getInformacion extends AsyncTask<Void, Void, Boolean>
    {
        protected void onPreExecute()
        {
            progress = new ProgressDialog(MusicoInformacion.this);
            progress.setMessage("Loading...");
            progress.setTitle("Obteniendo información");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String sql = SERVER+"/musico/"+idMusico;

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
                        //Log.d("**Informacion**",jsonAr.toString());
                        for (int i = 0; i < jsonAr.length(); i++)
                        {
                            JSONObject obj = (JSONObject) jsonAr.get(i);

                            for(int j = 0; j < obj.getJSONArray("instrumentos").length(); j++) {
                                JSONObject objInstrumentos = obj.getJSONArray("instrumentos").getJSONObject(j);
                                Instrumentos.add(objInstrumentos.getString("nombre"));
                            }

                            for(int j = 0; j < obj.getJSONArray("excepciones").length(); j++) {
                                JSONObject objExcepciones = obj.getJSONArray("excepciones").getJSONObject(j);
                                Excepciones.add(objExcepciones.getString("nombre"));
                            }

                            for(int j = 0; j< obj.getJSONArray("horarios").length(); j++)
                            {
                                JSONObject objHorarios = obj.getJSONArray("horarios").getJSONObject(j);
                                Horarios.add(new Horario_data(
                                        objHorarios.getString("horario_inicial"),
                                        objHorarios.getString("horario_final"),
                                        objHorarios.getInt("c_dia_id")
                                ));
                            }

                            informacion.add(new MusicoInformacion_data(
                                    obj.getString("nombre")+" "+obj.getString("apellido_paterno")+" "+obj.getString("apellido_materno"),
                                    obj.getString("telefono1"),
                                    obj.getString("telefono2"),
                                    obj.getString("disponibilidad")
                            ));
                            //Log.d("**Instrumentos",obj.getJSONArray("instrumentos").toString());

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

            return true;
        }

        protected void onPostExecute(Boolean result)
        {
            mostrarInformacion();
            progress.dismiss();
        }
    }
}
