package com.nemesis.centrodecontroluvandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import modelo.Alumno;
import modelo.Curso;

public class MenuCursoActivity extends AppCompatActivity {
    private Curso curso;
    private TextView lblNombreCurso;
    private TextView lblPeriodoCurso;
    private List<Alumno> listaAlumno;
    private ListView listaAlumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_curso);
        Bundle bundle = getIntent().getExtras();
        curso = (Curso) bundle.getSerializable("curso");
        new BuscarAlumnosTask(curso, this).execute();
        lblNombreCurso = findViewById(R.id.lblNombreCurso);
        lblNombreCurso.setText(curso.getNombre());
        lblPeriodoCurso = findViewById(R.id.lblPeriodoCurso);
        lblPeriodoCurso.setText(curso.getPeriodo());
        listaAlumnos = findViewById(R.id.listaAlumnos);
    }

    public void setAlumnos(List<Alumno> alumnos){
        this.listaAlumno = alumnos;
        AlumnosAdapter alumnosAdapter = new AlumnosAdapter(this,R.layout.listview_item_alumno_row,listaAlumno);
        listaAlumnos.setAdapter(alumnosAdapter);

        listaAlumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Alumno alumno = listaAlumno.get(posicion);
                Intent intent = new Intent(getApplicationContext(), ActividadesCursoActivity.class);
                intent.putExtra("curso",curso);
                intent.putExtra("alumno",alumno);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"recibido "+alumno.getNombre(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
/*-----------------------------------------*/
class BuscarAlumnosTask extends AsyncTask<Void, Void, Boolean> {
    private MenuCursoActivity menuCursoActivity;
    private List<Alumno> listaAlumnos = new ArrayList<>();
    private Curso curso;

    public BuscarAlumnosTask(Curso curso, MenuCursoActivity menuCursoActivity){
        this.curso = curso;
        this.menuCursoActivity = menuCursoActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://127.0.0.1:8080/AccesoBD2018/webresources/modelo.inscripcion/inscripciones/"+curso.getIdCurso();
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
            menuCursoActivity.setAlumnos(this.listaAlumnos);
        }
    }
}

