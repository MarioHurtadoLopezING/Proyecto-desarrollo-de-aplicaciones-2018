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

import modelo.Curso;
import modelo.Usuario;

public class CursosActivity extends AppCompatActivity {
    private Usuario usuario;
    private TextView txtNombre;
    private ListView listaCursos;
    private List<Curso> listaCursosRecibidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);
        txtNombre = findViewById(R.id.txtNombre);
        listaCursos = findViewById(R.id.listaCursos);
        Bundle bundle = getIntent().getExtras();
        usuario = (Usuario) bundle.getSerializable("usuario");
        txtNombre.setText("Profesor: "+usuario.getProfesor().getNombre());
        new BuscarCursosTask(usuario,this).execute();
    }
    public void setCursos(List<Curso> cursos){
        this.listaCursosRecibidos = cursos;
        CursosAdapter cursoAdapter = new CursosAdapter(this,R.layout.listview_item_curso_row,listaCursosRecibidos);
        listaCursos.setAdapter(cursoAdapter);

        listaCursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Curso curso = listaCursosRecibidos.get(posicion);
                mostrarAlumnos(view,curso);
            }
        });
    }
    public void mostrarAlumnos(View view, Curso curso){
        Intent intent = new Intent(this, MenuCursoActivity.class);
        intent.putExtra("curso",curso);
        startActivity(intent);
    }
}
class BuscarCursosTask extends AsyncTask<Void, Void, Boolean> {
    private Usuario usuario;
    private CursosActivity mainActivity;
    private List<Curso> listaCursos = new ArrayList<>();

    public BuscarCursosTask(Usuario usuario, CursosActivity mainActivity){
        this.usuario = usuario;
        this.mainActivity = mainActivity;
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
            mainActivity.setCursos(this.listaCursos);
        }
    }
}

