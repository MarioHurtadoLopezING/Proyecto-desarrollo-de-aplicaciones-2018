package com.nemesis.centrodecontroluvandroid.actividadesalumnos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nemesis.centrodecontroluvandroid.CursosActivity;
import com.nemesis.centrodecontroluvandroid.CursosAdapter;
import com.nemesis.centrodecontroluvandroid.MenuCursoActivity;
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
import modelo.Usuario;

/**
 * Created by Desktop on 10/06/2018.
 */

public class CursosProfesorActivity extends AppCompatActivity {
    private TextView txtCorreo;
    private Usuario usuario;
    private ListView listaCursosProfesor1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos_profesor);
        txtCorreo = findViewById(R.id.txtCorreo);
        Bundle bundle = getIntent().getExtras();
        usuario = (Usuario) bundle.getSerializable("usuario");
        txtCorreo.setText(usuario.getProfesor().getCorreo());
        listaCursosProfesor1 = findViewById(R.id.listaCursosProfesor1);
        new BuscarCursosTask(usuario,this).execute();

    }
    private List<Curso> cursos;
    public void setCursos(final List<Curso> cursos){
        this.cursos = cursos;
        CursosAdapter cursoAdapter = new CursosAdapter(this,R.layout.listview_item_curso_row,cursos);
        listaCursosProfesor1.setAdapter(cursoAdapter);

        listaCursosProfesor1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Curso curso = cursos.get(posicion);
                mostrarAlumnos(view,curso);
            }
        });
    }
    public void mostrarAlumnos(View view, Curso curso){
        Intent intent = new Intent(this, AlumnosProfesorActivity.class);
        intent.putExtra("curso",curso);
        startActivity(intent);
    }
}
//###################
class BuscarCursosTask extends AsyncTask<Void, Void, Boolean> {
    private Usuario usuario;
    private CursosProfesorActivity cursosProfesorActivity;
    private List<Curso> listaCursos = new ArrayList<>();

    public BuscarCursosTask(Usuario usuario, CursosProfesorActivity cursosProfesorActivity){
        this.usuario = usuario;
        this.cursosProfesorActivity = cursosProfesorActivity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String url = "http://192.168.100.7:8080/AccesoBD2018/webresources/modelo.curso/cursosProfesor/"+usuario.getProfesor().getIdUsuario();
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
            cursosProfesorActivity.setCursos(this.listaCursos);
        }
    }
}