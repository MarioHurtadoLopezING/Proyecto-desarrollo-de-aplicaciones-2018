package com.nemesis.centrodecontroluvandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import modelo.Usuario;

public class MainActivity extends AppCompatActivity {
    private String nombreUsuario;
    private String contrasena;
    private EditText txtUsuario;
    private EditText txtContrasena;
    private Button btnIniciarSesion;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*--------------------*/
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion(view);
            }
        });
        /*----------------------*/
    }
    public void iniciarSesion(View view){
        nombreUsuario = txtUsuario.getText().toString();
        contrasena = txtContrasena.getText().toString();
        Usuario usuario = new Usuario(0,nombreUsuario,contrasena);
        new IniciciarSesionTask(usuario,this).execute();
    }
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
        if(usuario != null) {//aqui lanzar la ventana principal de profesor y manarle el profesor recuperado
            System.out.println("El usuario es" + usuario.getUsuario() + "  " + usuario.getIdUsuario());
            Toast.makeText(this, "Bienvenido " + usuario.getProfesor().getCorreo(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MenuPrincipalActivity.class);
            intent.putExtra("usuario",usuario);
            startActivity(intent);
        }else{
            Toast.makeText(this, "usuario/contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
class IniciciarSesionTask extends AsyncTask<Void, Void, Boolean>{
    private Usuario usuario;
    private Usuario usuarioRespuesta;
    private MainActivity mainActivity;

    public IniciciarSesionTask(Usuario usuario, MainActivity mainActivity){
        this.usuario = usuario;
        this.mainActivity = mainActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://192.168.100.7:8080/AccesoBD2018/webresources/modelo.usuario/login/"+usuario.getUsuario()+"/"+usuario.getContrasena();
        try {
            URL ourl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            InputStream inputStream;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
                System.out.println("entro");
            } else {
                inputStream = conn.getErrorStream();
                System.out.println("algo malo sucedio");
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String cad = bufferedReader.readLine();
            JSONObject jsonRespuesta =new JSONObject(cad);
            System.out.println(jsonRespuesta);
            usuarioRespuesta = new Usuario(jsonRespuesta);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    protected void onPostExecute(final Boolean success){
        if (success) {
            mainActivity.setUsuario(this.usuarioRespuesta);
        }
    }
}
