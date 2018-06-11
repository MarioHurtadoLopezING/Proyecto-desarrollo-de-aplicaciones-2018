package com.nemesis.centrodecontroluvandroid.actividadesalumnos;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nemesis.centrodecontroluvandroid.ActividadesAdapter;
import com.nemesis.centrodecontroluvandroid.ActividadesCursoActivity;
import com.nemesis.centrodecontroluvandroid.R;
import com.nemesis.centrodecontroluvandroid.RegistrarActividadActivity;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import modelo.Actividad;
import modelo.Alumno;
import modelo.Curso;

public class ActividadesAlumnoActivity extends AppCompatActivity {
    private ListView listaActividadesAlumno;
    private Alumno alumno;
    private Curso curso;
    private List<Actividad> actividades;
    private List<Integer> puntaje;
    private TextView lblPorcentaje;
    private TextView lblCalificacionPrcial;
    private TextView lblNombre;
    private TextView lblCorreo;
    private TextView lblMatricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_alumno);
        listaActividadesAlumno = findViewById(R.id.listaActividadesAlumno);
        Bundle bundle = getIntent().getExtras();
        alumno = (Alumno) bundle.getSerializable("alumno");
        curso = (Curso) bundle.getSerializable("curso");
        lblPorcentaje = findViewById(R.id.lblPorcentaje);
        lblCalificacionPrcial = findViewById(R.id.lblCalificacionPrcial);
        lblNombre = findViewById(R.id.lblNombre);
        lblCorreo = findViewById(R.id.lblCorreo);
        lblMatricula = findViewById(R.id.lblMatricula);
        lblNombre.setText(alumno.getNombre());
        lblCorreo.setText(alumno.getCorreo());
        lblMatricula.setText(alumno.getMatricula());

        new BuscarActividadesTask(curso,alumno,this).execute();
    }
    public void setActividadesObtenidas(List<Actividad> actividades1, List<Integer> puntaje1){
        this.actividades = actividades1;
        int numero = 0;
        for(Actividad a:actividades){
            numero = numero + Integer.parseInt(a.getPorcentaje());
        }
        this.puntaje = puntaje1;
        int sumatoria = 0;
       //como saber si va a reprobar
        for (int i= 0; i< puntaje.size(); i++){
            sumatoria = sumatoria + puntaje.get(i);
        }
    double a = Double.parseDouble(Integer.toString(numero));
        a = a/100;
        if(sumatoria < (a*80)){
            lblCalificacionPrcial.setText("El alumno no cubre el 80% de participaciones, tiene "+sumatoria+" puntos de "+numero);
        }else{
            lblCalificacionPrcial.setText("El puntaje total es "+sumatoria+" puntos de "+sumatoria);
        }
        lblPorcentaje.setText("El puntaje obtenido es: "+sumatoria+" puntos");
        ActividadesAdapter actividadesAdapter = new ActividadesAdapter(this,R.layout.listview_item_actividad_row,actividades);
        listaActividadesAlumno.setAdapter(actividadesAdapter);

        listaActividadesAlumno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Actividad actividad = actividades.get(posicion);
                Toast.makeText(getApplicationContext(),"Puntaje obtenido "+ puntaje.get(posicion)+" puntos",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
//####################################################################################
class BuscarActividadesTask extends AsyncTask<Void, Void, Boolean> {
    private  ActividadesAlumnoActivity actividadesAlumnoActivity;
    private List<Actividad> listaActividades = new ArrayList<>();
    private List<Integer> puntaje = new ArrayList<>();

    private Curso curso;
    private Alumno alumno;

    public BuscarActividadesTask(Curso curso, Alumno alumno, ActividadesAlumnoActivity actividadesAlumnoActivity){
        this.curso = curso;
        this.alumno = alumno;
        this.actividadesAlumnoActivity = actividadesAlumnoActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://192.168.100.7:8080/AccesoBD2018/webresources/modelo.registroactividad/actividadesalumno/"+curso.getIdCurso()+"/"+alumno.getIdAlumno();
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
                JSONObject object = jsonRespuesta.getJSONObject(i).getJSONObject("idRegistro");
                listaActividades.add(new Actividad(object));
                puntaje.add(jsonRespuesta.getJSONObject(i).getInt(("porcentaje")));
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
            actividadesAlumnoActivity.setActividadesObtenidas(this.listaActividades,puntaje);
        }
    }
}