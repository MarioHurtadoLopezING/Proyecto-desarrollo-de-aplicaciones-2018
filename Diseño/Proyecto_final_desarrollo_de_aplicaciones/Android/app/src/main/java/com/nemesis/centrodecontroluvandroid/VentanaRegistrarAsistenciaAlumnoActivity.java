package com.nemesis.centrodecontroluvandroid;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nemesis.centrodecontroluvandroid.registroasistencias.RegistroAsistenciaAdapter;
import com.nemesis.centrodecontroluvandroid.registroasistencias.RegistroAsistenciasActivity;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Alumno;
import modelo.Curso;

public class VentanaRegistrarAsistenciaAlumnoActivity extends AppCompatActivity {
    private List<Alumno> listaAlumnos;
    private ListView lstaRegistroAsistencias;
    private TextView lblAsignatura;
    private TextView lblPeriodo;
    private Curso curso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_registrar_asistencia_alumno);
        lstaRegistroAsistencias = findViewById(R.id.lstaRegistroAsistencias);
        Bundle bundle = getIntent().getExtras();
        curso = (Curso) bundle.getSerializable("curso");
        lblAsignatura = findViewById(R.id.lblAsignatura);
        lblAsignatura.setText(curso.getNombre());
        lblPeriodo = findViewById(R.id.lblPeriodo);
        lblPeriodo.setText(curso.getPeriodo());
        new BuscarAlumnosActividadesTask(curso,this).execute();
    }

    public void setListaAlumnos(List<Alumno> listaAlumnos1){
        this.listaAlumnos = listaAlumnos1;
       for(Alumno al:listaAlumnos1){
           System.out.println(al.getNombre());
           //aqui la lista
           AlumnosAdapter alumnosAdapter = new AlumnosAdapter(this,R.layout.listview_item_alumno_row,listaAlumnos);
           lstaRegistroAsistencias.setAdapter(alumnosAdapter);

           lstaRegistroAsistencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                   Alumno alumno = listaAlumnos.get(posicion);
                    buscar(alumno);
               }
           });
       }
    }
    public void buscar(Alumno alumno){
        new RegistrarAsistenciaTask(alumno,curso, this).execute();
    }
    public void registroGuardado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Asistencia registrada")
            .setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

class BuscarAlumnosActividadesTask extends AsyncTask<Void, Void, Boolean> {
    private VentanaRegistrarAsistenciaAlumnoActivity ventanaRegistrarAsistenciaAlumnoActivity;
    private List<Alumno> listaAlumnos = new ArrayList<>();
    private Curso curso;

    public BuscarAlumnosActividadesTask(Curso curso, VentanaRegistrarAsistenciaAlumnoActivity ventanaRegistrarAsistenciaAlumnoActivity){
        this.curso = curso;
        this.ventanaRegistrarAsistenciaAlumnoActivity = ventanaRegistrarAsistenciaAlumnoActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://192.168.100.7:8080/AccesoBD2018/webresources/modelo.inscripcion/inscripciones/"+curso.getIdCurso();
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
            JSONArray jsonRespuesta =new JSONArray(cad);
            for(int i = 0; i < jsonRespuesta.length(); i++){//aqui mero---------------------
                JSONObject jsonAlumno = jsonRespuesta.getJSONObject(i).getJSONObject("idAlumno");
                listaAlumnos.add(new Alumno(jsonAlumno));
            }

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
            ventanaRegistrarAsistenciaAlumnoActivity.setListaAlumnos(this.listaAlumnos);
        }
    }
}
//##############################
class RegistrarAsistenciaTask extends AsyncTask<Void, Void, Boolean> {
    private String porcentaje;
    private Alumno alumno;
    private Curso curso;
    private VentanaRegistrarAsistenciaAlumnoActivity ventanaRegistrarAsistenciaAlumnoActivity;

    public RegistrarAsistenciaTask(Alumno alumno, Curso curso, VentanaRegistrarAsistenciaAlumnoActivity ventanaRegistrarAsistenciaAlumnoActivity){
        this.alumno = alumno;
        this.curso = curso;
        this.ventanaRegistrarAsistenciaAlumnoActivity = ventanaRegistrarAsistenciaAlumnoActivity;
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            String url = "http://192.168.100.7:8080/AccesoBD2018/webresources/modelo.asistencia";
            String metodoEnvio = "POST";

            URL ourl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(metodoEnvio);
            conn.connect();

            JSONObject jsonAlumno = new JSONObject();
            jsonAlumno.put("idAlumno",alumno.getIdAlumno());
            jsonAlumno.put("nombre",alumno.getNombre());
            jsonAlumno.put("correo",alumno.getCorreo());
            jsonAlumno.put("matricula",alumno.getMatricula());

            JSONObject jsonProfesor = new JSONObject();
            jsonProfesor.put("idProfesor",curso.getProfesor().getIdUsuario());
            jsonProfesor.put("nombre",curso.getProfesor().getNombre());
            jsonProfesor.put("Correo",curso.getProfesor().getCorreo());

            JSONObject jsonCurso = new JSONObject();
            jsonCurso.put("idCurso",curso.getIdCurso());
            jsonCurso.put("nombre",curso.getNombre());
            jsonCurso.put("periodo", curso.getPeriodo());
            jsonCurso.put("idProfesor", jsonProfesor);

            JSONObject jsonAsistencia = new JSONObject();
            jsonAsistencia.put("idAsistencia",0);
            jsonAsistencia.put("fecha",new Date());
            jsonAsistencia.put("idAlumno",jsonAlumno);
            jsonAsistencia.put("idCurso",jsonCurso);
            System.out.println(jsonAsistencia);

            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(outputStream));
            escritor.write(jsonAsistencia.toString());
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
            this.ventanaRegistrarAsistenciaAlumnoActivity.registroGuardado();
        }
    }
}