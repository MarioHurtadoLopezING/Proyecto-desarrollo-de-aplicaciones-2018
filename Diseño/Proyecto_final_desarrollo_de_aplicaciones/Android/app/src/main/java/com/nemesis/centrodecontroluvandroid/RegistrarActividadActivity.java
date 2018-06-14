package com.nemesis.centrodecontroluvandroid;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import modelo.Actividad;
import modelo.Alumno;
import modelo.Curso;

public class RegistrarActividadActivity extends AppCompatActivity {
    private EditText lblNombreActvidad;
    private EditText lblFechaInicio;
    private EditText lblFechaFin;
    private EditText lblPorcentaje;
    private EditText lblDescripcion;
    private EditText lblCalificacionFinal;
    private RadioButton radioEntregado;
    private RadioButton radioNoEntregado;
    private Button btnRegistrarActividad;
    private Actividad actividad;
    private Alumno alummno;
    private Curso curso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_actividad);
        lblNombreActvidad = findViewById(R.id.lblNombreActvidad);
        lblFechaInicio = findViewById(R.id.lblFechaInicio);
        lblFechaFin = findViewById(R.id.lblFechaFin);
        lblPorcentaje = findViewById(R.id.lblPorcentaje);
        lblDescripcion = findViewById(R.id.lblDescripcion);
        lblCalificacionFinal = findViewById(R.id.lblCalificacionFinal);
        radioEntregado = findViewById(R.id.radioEntregado);
        radioNoEntregado = findViewById(R.id.radioNoEntregado);
        btnRegistrarActividad = findViewById(R.id.btnRegistrarActividad);
        btnRegistrarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registrarActividad(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        actividad = (Actividad) bundle.getSerializable("actividad");
        alummno = (Alumno) bundle.getSerializable("alumno");
        curso = (Curso) bundle.getSerializable("curso");
        /*-------------------------------------*/
        lblNombreActvidad.setText(actividad.getNombre());
        lblFechaInicio.setText(getFecha(actividad.getFechaInicio()));
        lblFechaFin.setText(getFecha(actividad.getFechaFin()));
        lblPorcentaje.setText(actividad.getPorcentaje()+" puntos");
        lblDescripcion.setText(actividad.getDescripcion());
    }
    private String getFecha(String fch){
        String[] parts = fch.split("-");
        String aux = parts[2];
        String[] ax = aux.split("T");


        return ax[0]+"-"+parts[1]+"-"+parts[0];

    }
    public void registrarActividad(View view) throws JSONException {
        new BuscarCursoTask(this.curso,this).execute();
        new BuscarActividadTask(this.actividad,this).execute();
        new BuscarAlumnoTask(this.alummno,this).execute();
    }
    public void registroGuardado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Actividad registrada exitosamente")
            .setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private JSONObject jsonAlumno;
    private JSONObject jsonCurso;
    private JSONObject jsonActividad;
    public void setJsonCurso(JSONObject jsonCurso){
        this.jsonCurso = jsonCurso;
    }
    public void setJsonActividad(JSONObject jsonActividad){
        this.jsonActividad = jsonActividad;
    }
    public void setJsonAlumno(JSONObject jsonAlumno){
        this.jsonAlumno = jsonAlumno;
        guardar();
    }
    public void guardar(){
        String porcentaje = lblCalificacionFinal.getText().toString();
        new GuardarAlumnosTask(jsonAlumno,jsonCurso,jsonActividad,porcentaje,this).execute();
    }
}
//--------------------------
class GuardarAlumnosTask extends AsyncTask<Void, Void, Boolean> {
    private JSONObject jsonAlumno;
    private JSONObject jsonCurso;
    private JSONObject jsonActividad;
    private String porcentaje;
    private RegistrarActividadActivity registrarActividadActivity;

    public GuardarAlumnosTask(JSONObject jsonAlumno,JSONObject jsonCurso, JSONObject jsonActividad, String porcentaje, RegistrarActividadActivity registrarActividadActivity){
        this.jsonAlumno = jsonAlumno;
        this.jsonCurso = jsonCurso;
        this.porcentaje = porcentaje;
        this.jsonActividad = jsonActividad;
        System.out.println(porcentaje);
        this.registrarActividadActivity = registrarActividadActivity;
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            String url = "http://127.0.0.1:8080/AccesoBD2018/webresources/modelo.registroactividad";
            String metodoEnvio = "POST";

            URL ourl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(metodoEnvio);
            conn.connect();

            JSONObject object = new JSONObject();
            object.put("idActividad",0);
            object.put("idAlumno",jsonAlumno);
            object.put("idCurso",jsonCurso);
            object.put("porcentaje",porcentaje);
            object.put("idRegistro",jsonActividad);
            System.out.println(object);
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(outputStream));
            escritor.write(object.toString());
            escritor.flush();

            InputStream inputStream;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String cad = bufferedReader.readLine();
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
    protected void onPostExecute(final Boolean success) {

        if (success) {
            this.registrarActividadActivity.registroGuardado();
        }
    }
}
//------------------------------------
class BuscarCursoTask extends AsyncTask<Void, Void, Boolean> {
    private Curso curso;
    private RegistrarActividadActivity  registrarActividadActivity;
    private JSONObject jsonRespuesta;

    public BuscarCursoTask(Curso curso, RegistrarActividadActivity  registrarActividadActivity){
        this.curso = curso;
        this.registrarActividadActivity = registrarActividadActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://127.0.0.1:8080/AccesoBD2018/webresources/modelo.curso/"+curso.getIdCurso();
        try {
            URL ourl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            InputStream inputStream;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String cad = bufferedReader.readLine();
            jsonRespuesta =new JSONObject(cad);
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
            registrarActividadActivity.setJsonCurso(jsonRespuesta);
        }
    }
}
//-----------------------------
class BuscarAlumnoTask extends AsyncTask<Void, Void, Boolean> {
    private Alumno alumno;
    private RegistrarActividadActivity  registrarActividadActivity;
    private JSONObject jsonRespuesta;

    public BuscarAlumnoTask(Alumno alumno, RegistrarActividadActivity  registrarActividadActivity){
        this.alumno = alumno;
        this.registrarActividadActivity = registrarActividadActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://127.0.0.1:8080/AccesoBD2018/webresources/modelo.alumno/"+alumno.getIdAlumno();
        try {
            URL ourl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            InputStream inputStream;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String cad = bufferedReader.readLine();
            jsonRespuesta =new JSONObject(cad);
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
            registrarActividadActivity.setJsonAlumno(jsonRespuesta);
        }
    }
}
//----------------------------------
class BuscarActividadTask extends AsyncTask<Void, Void, Boolean> {
    private Actividad actividad;
    private RegistrarActividadActivity  registrarActividadActivity;
    private JSONObject jsonRespuesta;

    public BuscarActividadTask(Actividad actividad, RegistrarActividadActivity  registrarActividadActivity){
        this.actividad = actividad;
        this.registrarActividadActivity = registrarActividadActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://127.0.0.1:8080/AccesoBD2018/webresources/modelo.actividad/"+actividad.getIdActividad();
        try {
            URL ourl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            InputStream inputStream;
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String cad = bufferedReader.readLine();
            jsonRespuesta =new JSONObject(cad);
            System.out.println(jsonRespuesta);
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
            registrarActividadActivity.setJsonActividad(jsonRespuesta);
        }
    }
}

