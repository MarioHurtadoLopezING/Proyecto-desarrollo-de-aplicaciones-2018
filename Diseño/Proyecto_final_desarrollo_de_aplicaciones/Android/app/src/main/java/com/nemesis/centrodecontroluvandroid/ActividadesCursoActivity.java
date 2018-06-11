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

import modelo.Actividad;
import modelo.Alumno;
import modelo.Curso;

public class ActividadesCursoActivity extends AppCompatActivity {
    private List<Actividad> actividadesObtenidas;
    private ListView listaActividades;
    private TextView lblNombreAlumno;
    private TextView lblCorreoAlumno;
    private TextView lblMatricula;
    private Alumno alumno;
    private Curso curso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_curso);
        listaActividades = findViewById(R.id.listaActividades);
        Bundle bundle = getIntent().getExtras();
        curso = (Curso) bundle.getSerializable("curso");
        alumno = (Alumno) bundle.getSerializable("alumno");
        lblNombreAlumno = findViewById(R.id.lblNombreAlumno);
        lblCorreoAlumno = findViewById(R.id.lblCorreoAlumno);
        lblMatricula = findViewById(R.id.lblMatricula);
        lblNombreAlumno.setText(alumno.getNombre());
        lblCorreoAlumno.setText(alumno.getCorreo());
        lblMatricula.setText(alumno.getMatricula());
        new BuscarActividadesTask(curso,this).execute();

    }

    public void setActividadesObtenidas(List<Actividad> actividadesObtenida){
        this.actividadesObtenidas = actividadesObtenida;
        ActividadesAdapter actividadesAdapter = new ActividadesAdapter(this,R.layout.listview_item_actividad_row,actividadesObtenidas);
        listaActividades.setAdapter(actividadesAdapter);

        listaActividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Actividad actividad = actividadesObtenidas.get(posicion);
                mostrarActividad(view,actividad,alumno);
            }
        });
    }
    public void mostrarActividad(View view, Actividad actividad,Alumno alumno){//debo mandarle actividad, curso, porcentaje//RegistrarActividadActivity
        //this.curso;//---------------------------------------------------------------------------------------------------------------
        Intent intent = new Intent(this, RegistrarActividadActivity.class);
        intent.putExtra("actividad",actividad);
        intent.putExtra("curso",curso);
        intent.putExtra("alumno",alumno);
        startActivity(intent);
        Toast.makeText(this,"mensaje de "+actividad.getDescripcion(),Toast.LENGTH_SHORT).show();
    }
//-----------
}
class BuscarActividadesTask extends AsyncTask<Void, Void, Boolean> {
    private ActividadesCursoActivity actividadesCursoActivity;
    private List<Actividad> listaActividades = new ArrayList<>();
    private Curso curso;

    public BuscarActividadesTask(Curso curso, ActividadesCursoActivity actividadesCursoActivity){
        this.curso = curso;
        this.actividadesCursoActivity = actividadesCursoActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://192.168.100.7:8080/AccesoBD2018/webresources/modelo.actividad/actividadesCurso/"+curso.getIdCurso();
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
                listaActividades.add(new Actividad(jsonRespuesta.getJSONObject(i)));
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
            actividadesCursoActivity.setActividadesObtenidas(this.listaActividades);
        }
    }
}

