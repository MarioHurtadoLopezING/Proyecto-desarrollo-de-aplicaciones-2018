package com.nemesis.centrodecontroluvandroid.registroasistencias;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.nemesis.centrodecontroluvandroid.CursosAdapter;
import com.nemesis.centrodecontroluvandroid.R;

import java.util.List;

import modelo.Alumno;
import modelo.Curso;

/**
 * Created by Desktop on 12/06/2018.
 */

public class RegistroAsistenciaAdapter extends ArrayAdapter<Alumno> {
    private Context context;
    private int resource;
    private List<Alumno> listaAlumnos;
    public RegistroAsistenciaAdapter(@NonNull Context context, int resource, List<Alumno> listaAlumnos) {
        super(context, resource, listaAlumnos);
        this.context = context;
        this.resource = resource;
        this.listaAlumnos = listaAlumnos;
    }
    public View getView(int posicion, View convertView, ViewGroup parent){
        View row = convertView;
        RegistroAsistenciaAdapter.AsistenciaHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource,parent,false);
            holder = new RegistroAsistenciaAdapter.AsistenciaHolder();
            holder.txtNombreAlumno = (TextView) row.findViewById(R.id.txtNombreAlumno);
            row.setTag(holder);
        }else{
            holder = (RegistroAsistenciaAdapter.AsistenciaHolder) row.getTag();
        }
        Alumno  alumno = listaAlumnos.get(posicion);
        holder.txtNombreAlumno.setText(alumno.getNombre());
        return row;
    }
    static class AsistenciaHolder{
        TextView txtNombreAlumno;
    }
}