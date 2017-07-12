package com.example.jesus.tumusicoideal;

import android.app.ProgressDialog;
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
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RegistroCliente extends AppCompatActivity{
    //Variables
    public final String SERVER = "https://jesusequihua.000webhostapp.com/tumusicoapi";
    private static final int PHOTO_SELECTED = 1;
    private Uri url_imagen;

    //Se pone en -1 para evitar problemas al crear los MaterialBetterSpinner
    int idPais = -1;
    int idEstado = -1;
    int idCiudad = -1;

    //Imagen
    String encodedImage;

    //Cadenas para enviar en el JSON
    String nombre_str, apellidoPaterno_str, apellidoMaterno_str, telefono_str,
           telefonoOpcional_str, usuario_str, password_str;


    Toolbar toolbar;
    Button btnGaleria, btnRegistro;
    ImageView imagePreview;
    EditText nombre,apellidoPaterno,apellidoMaterno,
            telefono,telefonoOpcional,usuario,password,passwordAgain;

    MaterialBetterSpinner spinnerPais;
    MaterialBetterSpinner spinnerEstado;
    MaterialBetterSpinner spinnerCiudad;

    ArrayAdapter<Paises> arrayAdapterPais;
    ArrayAdapter<Estados> arrayAdapterEstado;
    ArrayAdapter<Ciudades> arrayAdapterCiudad;

    public ArrayList<Paises> PaisDatos = new ArrayList<Paises>();
    public ArrayList<Estados> EstadoDatos = new ArrayList<Estados>();
    public ArrayList<Ciudades> CiudadDatos = new ArrayList<Ciudades>();

    ProgressDialog progress;

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

        new GetPaises().execute(); //Obtener los paises registrados

        initSpinners(); //Inicializa los Spinners

        //Seleccionar imagen de galeria
        btnGaleria = (Button) findViewById(R.id.btn_abrirGaleria);
        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Seleccione una imagen"),
                        PHOTO_SELECTED);
            }
        });
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        /*Datos de registro*/
        nombre = (EditText) findViewById(R.id.campo_nombre);
        apellidoPaterno = (EditText) findViewById(R.id.campo_apellidoP);
        apellidoMaterno = (EditText) findViewById(R.id.campo_apellidoM);
        telefono = (EditText) findViewById(R.id.campo_telefono);
        telefonoOpcional = (EditText) findViewById(R.id.campo_telefonoOpcional);
        usuario = (EditText) findViewById(R.id.campo_usuario);
        password = (EditText) findViewById(R.id.campo_password);
        passwordAgain = (EditText) findViewById(R.id.campo_password2);

        btnRegistro = (Button) findViewById(R.id.boton_aceptar);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si las contraseñas no coinciden manda un toast
                if(! password.getText().toString().equals(passwordAgain.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                }else{
                    //Si si coincidieron obtiene los datos de los campos
                    //Toast.makeText(getApplicationContext(),"Enviando datos",Toast.LENGTH_LONG).show();
                    nombre_str = nombre.getText().toString();
                    apellidoPaterno_str = apellidoPaterno.getText().toString();
                    apellidoMaterno_str = apellidoMaterno.getText().toString();
                    telefono_str = telefono.getText().toString();
                    telefonoOpcional_str = telefonoOpcional.getText().toString();
                    usuario_str = usuario.getText().toString();
                    password_str = password.getText().toString();
                    imagePreview.buildDrawingCache();
                    Bitmap bm = imagePreview.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();

                    encodedImage = Base64.encodeToString(b , Base64.DEFAULT);

                    new RegistrarMusico().execute(); //Manda llamar el servicio para registrar un nuevo musico
                }
            }
        });
    }

    protected  void initSpinners()
    {
        spinnerPais =  (MaterialBetterSpinner) findViewById(R.id.spinnerPais);
        spinnerEstado = (MaterialBetterSpinner) findViewById(R.id.spinnerEstado);
        spinnerCiudad = (MaterialBetterSpinner) findViewById(R.id.spinnerCiudad);

        arrayAdapterPais = new ArrayAdapter<Paises>(this,android.R.layout.simple_dropdown_item_1line, PaisDatos);
        arrayAdapterEstado = new ArrayAdapter<Estados>(RegistroCliente.this, android.R.layout.simple_dropdown_item_1line, EstadoDatos);
        arrayAdapterCiudad = new ArrayAdapter<Ciudades>(RegistroCliente.this, android.R.layout.simple_dropdown_item_1line, CiudadDatos);

        spinnerPais.setAdapter(arrayAdapterPais);
        spinnerEstado.setAdapter(arrayAdapterEstado);
        spinnerCiudad.setAdapter(arrayAdapterCiudad);

        spinnerPais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idPais = ((Paises) parent.getItemAtPosition(position)).getId();
                EstadoDatos.clear();
                new GetEstados().execute();
                arrayAdapterEstado = new ArrayAdapter<Estados>(RegistroCliente.this, android.R.layout.simple_dropdown_item_1line, EstadoDatos);
                spinnerEstado.setAdapter(arrayAdapterEstado);
            }
        });

        spinnerEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idEstado = ((Estados) parent.getItemAtPosition(position)).getId();
                CiudadDatos.clear();
                new GetCiudades().execute();
                arrayAdapterCiudad = new ArrayAdapter<Ciudades>(RegistroCliente.this, android.R.layout.simple_dropdown_item_1line, CiudadDatos);
                spinnerCiudad.setAdapter(arrayAdapterCiudad);
            }
        });

        spinnerCiudad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idCiudad = ((Ciudades) parent.getItemAtPosition(position)).getId();
            }
        });

    }

    public void enviarAPrincipal()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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

    public void mostrarPreview() throws FileNotFoundException
    {
        if(url_imagen.toString().equalsIgnoreCase("NO"))
            return;
        InputStream stream = RegistroCliente.this.getContentResolver().openInputStream(url_imagen);
        BufferedInputStream buffer = new BufferedInputStream(stream);
        Bitmap imagen = BitmapFactory.decodeStream(buffer);
        imagePreview.setImageBitmap(imagen);
    }

    private class RegistrarMusico extends AsyncTask<String, Integer, String>
    {
        protected void onPreExecute()
        {
            progress = new ProgressDialog(RegistroCliente.this);
            progress.setMessage("Loading...");
            progress.setTitle("Registrando músico!");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
           try {
               //URL a la cual hacer la peticion
               URL url = new URL(SERVER+"/musico/new");

               //Guardar los datos en la variable de tipo JSON
               JSONObject postDataParams = new JSONObject();
               postDataParams.put("nombre",nombre_str);
               postDataParams.put("apellido_paterno",apellidoPaterno_str);
               postDataParams.put("apellido_materno",apellidoMaterno_str);
               postDataParams.put("telefono1",telefono_str);
               postDataParams.put("telefono2",telefonoOpcional_str);
               postDataParams.put("disponibilidad",1);
               postDataParams.put("foto_perfil",encodedImage);
               postDataParams.put("usuario",usuario_str);
               postDataParams.put("password",password_str);
               postDataParams.put("fecha_registro","2020-12-12");
               postDataParams.put("destacado","1");
               postDataParams.put("fecha_destacado","2020-12-12");
               postDataParams.put("c_pais_id",idPais);
               postDataParams.put("c_estado_id",idEstado);
               postDataParams.put("c_ciudad_id",idCiudad);

               Log.d("params", postDataParams.toString());

               //Hacer la conexion al servidor
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setReadTimeout(15000 /* milliseconds */);
               conn.setConnectTimeout(15000 /* milliseconds */);
               conn.setRequestMethod("POST");
               conn.setDoInput(true);
               conn.setDoOutput(true);
               OutputStream os = conn.getOutputStream();
               BufferedWriter writer = new BufferedWriter(
                       new OutputStreamWriter(os, "UTF-8"));
               writer.write(getPostDataString(postDataParams));

               writer.flush();
               writer.close();
               os.close();

               //Si existe una respuesta aqui se obtiene
               int responseCode=conn.getResponseCode();
               if (responseCode == HttpsURLConnection.HTTP_OK) {

                   BufferedReader in=new BufferedReader(
                           new InputStreamReader(
                                   conn.getInputStream()));
                   StringBuffer sb = new StringBuffer("");
                   String line="";

                   while((line = in.readLine()) != null) {
                       sb.append(line);
                       break;
                   }

                   in.close();
                   return sb.toString();
               }
               else {
                   return new String("false : "+responseCode);
               }
           }
           catch(Exception e){
               e.printStackTrace();
           }
            return null;
        }

        protected void onPostExecute(String result){
            progress.dismiss();
            enviarAPrincipal();
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }

    }

    private class GetPaises extends AsyncTask<Void, Void, Boolean>
    {
        protected void onPreExecute()
        {
            progress = new ProgressDialog(RegistroCliente.this);
            progress.setMessage("Loading...");
            progress.setTitle("Obteniendo país!");
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String sql = SERVER+"/ubicacion/paises";

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

            return true;
        }

        protected void onPostExecute(Boolean result){
            progress.dismiss();
        }
    }

    private class GetEstados extends AsyncTask<Void, Void, Boolean>
    {
        protected void onPreExecute()
        {
            progress = new ProgressDialog(RegistroCliente.this);
            progress.setMessage("Loading...");
            progress.setTitle("Obteniendo Estados!");
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String sql = SERVER+"/ubicacion/estados/"+idPais;

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
                            Estados estado = new Estados(obj.getInt("id"), obj.getString("estado"));
                            //Log.d("**TAG***", pais.getId()+"");
                            EstadoDatos.add(estado);
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

        protected void onPostExecute(Boolean result){
            progress.dismiss();
        }
    }

    private class GetCiudades extends AsyncTask<Void, Void, Boolean>
    {

        protected void onPreExecute()
        {
            progress = new ProgressDialog(RegistroCliente.this);
            progress.setMessage("Loading...");
            progress.setTitle("Obteniendo Ciudades!");
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String sql = SERVER+"/ubicacion/ciudades/"+idEstado;

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
                            Ciudades ciudad = new Ciudades(obj.getInt("id"), obj.getString("ciudad"));
                            //Log.d("**TAG***", pais.getId()+"");
                            CiudadDatos.add(ciudad);
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

        protected void onPostExecute(Boolean result){
            progress.dismiss();
        }
    }

}


