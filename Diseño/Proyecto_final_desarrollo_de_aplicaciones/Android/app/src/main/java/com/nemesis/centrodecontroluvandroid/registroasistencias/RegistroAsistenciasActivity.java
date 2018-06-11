package com.nemesis.centrodecontroluvandroid.registroasistencias;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nemesis.centrodecontroluvandroid.CursosActivity;
import com.nemesis.centrodecontroluvandroid.CursosAdapter;
import com.nemesis.centrodecontroluvandroid.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import modelo.Alumno;
import modelo.Curso;
import modelo.Profesor;

public class RegistroAsistenciasActivity extends AppCompatActivity {
    private Profesor profesor;
    private ListView listaCursosAsistencia;
    private List<Curso> cursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_asistencias);
        Bundle bundle = getIntent().getExtras();
        profesor = (Profesor) bundle.getSerializable("profesor");
        listaCursosAsistencia = findViewById(R.id.listaCursosAsistencia);
        new BuscarCursosTask(profesor,this).execute();
    }

    public void setCursos(List<Curso> cursos1){
        this.cursos = cursos1;
        CursosAdapter cursoAdapter = new CursosAdapter(this,R.layout.listview_item_curso_row,cursos);
        listaCursosAsistencia.setAdapter(cursoAdapter);

        listaCursosAsistencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Curso curso = cursos.get(posicion);
                mostrarAlumnos(view,curso);
            }
        });
    }
    public void mostrarAlumnos(View view, Curso curso){
        
    }
}
class BuscarCursosTask extends AsyncTask<Void, Void, Boolean> {
    private Profesor profesor;
    private RegistroAsistenciasActivity registroAsistenciasActivity;
    private List<Curso> listaCursos = new ArrayList<>();

    public BuscarCursosTask(Profesor profesor,  RegistroAsistenciasActivity registroAsistenciasActivity){
        this.profesor = profesor;
        this.registroAsistenciasActivity = registroAsistenciasActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://192.168.100.7:8080/AccesoBD2018/webresources/modelo.curso/cursosProfesor/"+profesor.getIdUsuario();
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
            for(int i = 0; i < jsonRespuesta.length(); i++){
                listaCursos.add(new Curso(jsonRespuesta.getJSONObject(i)));
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
            registroAsistenciasActivity.setCursos(this.listaCursos);
        }
    }
}
