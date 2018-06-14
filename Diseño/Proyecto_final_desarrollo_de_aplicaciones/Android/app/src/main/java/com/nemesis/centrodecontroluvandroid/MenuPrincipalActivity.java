package com.nemesis.centrodecontroluvandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nemesis.centrodecontroluvandroid.actividadesalumnos.CursosProfesorActivity;
import com.nemesis.centrodecontroluvandroid.registroasistencias.RegistroAsistenciasActivity;

import modelo.Usuario;

public class MenuPrincipalActivity extends AppCompatActivity {
    private Usuario usuario;
    private EditText txtNombre;
    private EditText txtCorreo;
    private ImageButton btnCursos;
    private ImageButton btnActividades;
    private ImageButton btnAlumnos;
    private ImageView imagenUsuario;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Bundle bundle = getIntent().getExtras();
        usuario = (Usuario) bundle.getSerializable("usuario");
        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        btnCursos = findViewById(R.id.btnCursos);
        btnActividades = findViewById(R.id.btnActividades);
        btnAlumnos = findViewById(R.id.btnAlumnos);
        imagenUsuario = findViewById(R.id.imagenUsuario);
        imagenUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePicture.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        txtNombre.setText(usuario.getProfesor().getNombre());
        txtCorreo.setText(usuario.getProfesor().getCorreo());
        btnActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actividades(view);
            }
        });
        btnCursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursos(view);
            }
        });
        btnAlumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alumnos(view);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            imagenUsuario.setImageBitmap(imagen);
        }
    }
    public void actividades(View view){
        Intent intent = new Intent(this, CursosActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void cursos(View view){
        Intent intent = new Intent(this, CursosProfesorActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }
    public void alumnos(View view){
        Intent intent = new Intent(this, RegistroAsistenciasActivity.class);
        intent.putExtra("profesor",usuario.getProfesor());
        startActivity(intent);
    }
}
