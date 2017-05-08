package com.example.jesus.tumusicoideal;

import android.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
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
        toolbar.setTitle(R.string.titleMainToolbar);
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

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            Toast.makeText(MainActivity.this, "Refresh App", Toast.LENGTH_LONG).show();
        }
        if(id == R.id.action_new){
            Toast.makeText(MainActivity.this, "Create Text", Toast.LENGTH_LONG).show();
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

}
