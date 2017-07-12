package com.example.jesus.tumusicoideal;

import android.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public final String SERVER = "https://jesusequihua.000webhostapp.com/tumusicoapi";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    private RecyclerView reciclerView;
    LinearLayoutManager linearManager;

    ProgressDialog progress;

    private List<Musico> musicos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.titleMainToolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        // Establecemos el actionbarToggle al drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // Llamamos a la funcion syncState para que se muestre nuestro icono del menu.
        actionBarDrawerToggle.syncState();

        // Establecemos un Listener para ejecutar una acción cuando un item es selccionado
        // como lo implementamos a nuestra activity pasamos como parametro nuestra clase MainActivity|this
        navigationView.setNavigationItemSelectedListener(this);

        musicos = new ArrayList<>();
        new getMusicos().execute();

        //RecyclerView
        reciclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearManager = new LinearLayoutManager(this);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
        reciclerView.setLayoutManager(linearManager);
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_new){
            Toast.makeText(MainActivity.this, "Create Text", Toast.LENGTH_LONG).show();
        }
        if(id == R.id.Actualizar){
            musicos.clear();
            new getMusicos().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        // Obtenemos el titulo del item seleccionado
        int id = menuItem.getItemId();

        // Actualizamos el fragment con el titulo del item
        displayFragment(id);
        return false;
    }

    // Cuando se presione el boton para regresar y el Navigation View esta abierto lo cerramos
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displayFragment(int menuItem) {
        if(menuItem == R.id.group_registroUsuario){
            drawerLayout.closeDrawers();
            Intent intent = new Intent(this, RegistroCliente.class);
            startActivity(intent);
        }
        else if(menuItem == R.id.group_loginUsuario){
            drawerLayout.closeDrawers();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    //Clase para obtener los musicos registrados
    private class getMusicos extends AsyncTask<Void, Integer, Boolean>
    {
        protected void onPreExecute(){
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setTitle("Obteniendo músicos");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String sql = SERVER+"/musico/all";

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
                        //Log.d("**MUSICOS**",jsonAr.toString());
                        for (int i = 0; i < jsonAr.length(); i++)
                        {
                            JSONObject obj = (JSONObject) jsonAr.get(i);
                            musicos.add(new Musico(obj.getString("id"), obj.getString("nombre")+" "+obj.getString("apellido_paterno")+" "+obj.getString("apellido_materno"), obj.getString("telefono1")));
                            //Log.d("***dasds***",obj.getString("nombre")+" "+obj.getString("apellido_paterno")+" "+obj.getString("apellido_materno")+" "+obj.getString("telefono1"));
                        }
                       // Log.d("AUN SIGUE","SIMON");
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
            if(result){
                RVAdapter adapter = new RVAdapter(musicos);
                reciclerView.setAdapter(adapter);
                progress.dismiss();
            }
        }
    }

}
