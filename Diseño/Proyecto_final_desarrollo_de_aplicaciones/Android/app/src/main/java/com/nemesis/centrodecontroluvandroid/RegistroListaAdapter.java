package com.nemesis.centrodecontroluvandroid;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import modelo.Alumno;

/**
 * Created by Desktop on 10/06/2018.
 */

public class RegistroListaAdapter extends ArrayAdapter<Alumno> {
    private Context context;
    private int resource;
    private List<Alumno> listaAlumnos;

    public RegistroListaAdapter(@NonNull Context context, int resource, List<Alumno> listaAlumnos) {
        super(context, resource, listaAlumnos);
        this.context = context;
        this.resource = resource;
        this.listaAlumnos = listaAlumnos;
    }
    public View getView(int posicion, View convertView, ViewGroup parent){
        View row = convertView;
        AlumnosAdapter.AlumnoHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource,parent,false);
            holder = new AlumnosAdapter.AlumnoHolder();
            holder.txtCorreoAlumno = (TextView) row.findViewById(R.id.txtCorreoAlumno);
            holder.txtNombreAlumno = (TextView) row.findViewById(R.id.txtNombreAlumno);
            holder.txtMatriculaAlumno = (TextView) row.findViewById(R.id.txtMatriculaAlumno);
            row.setTag(holder);
        }else{
            holder = ( AlumnosAdapter.AlumnoHolder) row.getTag();
        }
        Alumno alumno = listaAlumnos.get(posicion);
        holder.txtCorreoAlumno.setText(alumno.getCorreo());
        holder.txtNombreAlumno.setText(alumno.getNombre());
        holder.txtMatriculaAlumno.setText(alumno.getMatricula());
        return row;
    }
    static class AlumnoHolder{
        TextView txtCorreoAlumno;
        TextView txtNombreAlumno;

    }
}