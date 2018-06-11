package com.nemesis.centrodecontroluvandroid.actividadesalumnos;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nemesis.centrodecontroluvandroid.AlumnosAdapter;
import com.nemesis.centrodecontroluvandroid.R;

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

public class AlumnosProfesorActivity extends AppCompatActivity {
    private List<Alumno> listaAlumno;
    private ListView listaActividadesAlumno;
    private TextView lblNombreCurso;
    private TextView lblPeriodoCurso;
    private TextView lblProfesor;
    private Curso curso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos_profesor);
        Bundle bundle = getIntent().getExtras();
        curso = (Curso) bundle.getSerializable("curso");
        listaActividadesAlumno = findViewById(R.id.listaActividadesAlumno);
        lblNombreCurso = findViewById(R.id.lblNombreCurso);
        lblPeriodoCurso = findViewById(R.id.lblPeriodoCurso);
        lblProfesor = findViewById(R.id.lblProfesor);
        lblNombreCurso.setText("Nombre: "+curso.getNombre());
        lblPeriodoCurso.setText("Periodo: "+curso.getPeriodo());
        lblProfesor.setText("Profesor: "+ curso.getProfesor().getNombre());
        new BuscarAlumnosTask(curso, this).execute();
    }

    public void setAlumnos(List<Alumno> alumnos){
        this.listaAlumno = alumnos;
        AlumnosAdapter alumnosAdapter = new AlumnosAdapter(this,R.layout.listview_item_alumno_row,listaAlumno);
        listaActividadesAlumno.setAdapter(alumnosAdapter);

        listaActividadesAlumno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Intent intent = new Intent(getApplicationContext(), ActividadesAlumnoActivity.class);
                intent.putExtra("curso",curso);
                intent.putExtra("alumno",listaAlumno.get(posicion));
                startActivity(intent);
            }
        });
    }
}
/*-----------------------------------------*/
class BuscarAlumnosTask extends AsyncTask<Void, Void, Boolean> {
    private AlumnosProfesorActivity alumnosProfesorActivity;
    private List<Alumno> listaAlumnos = new ArrayList<>();
    private Curso curso;

    public BuscarAlumnosTask(Curso curso, AlumnosProfesorActivity alumnosProfesorActivity){
        this.curso = curso;
        this.alumnosProfesorActivity = alumnosProfesorActivity;
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
            for(int i = 0; i < jsonRespuesta.length(); i++){
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
            alumnosProfesorActivity.setAlumnos(this.listaAlumnos);
        }
    }
}


