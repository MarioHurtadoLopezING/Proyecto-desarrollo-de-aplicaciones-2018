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
import modelo.Curso;

/**
 * Created by Desktop on 07/06/2018.
 */

public class CursosAdapter extends ArrayAdapter<Curso> {
    private Context context;
    private int resource;
    private List<Curso> listaCursos;
    public CursosAdapter(@NonNull Context context, int resource, List<Curso> listaCursos) {
        super(context, resource, listaCursos);
        this.context = context;
        this.resource = resource;
        this.listaCursos = listaCursos;
    }
    public View getView(int posicion, View convertView, ViewGroup parent){
        View row = convertView;
        CursoHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource,parent,false);
            holder = new CursoHolder();
            holder.txtNombreCurso = (TextView) row.findViewById(R.id.txtNombreCurso);
            holder.txtPeriodo = (TextView) row.findViewById(R.id.txtPeriodo);
            row.setTag(holder);
        }else{
            holder = (CursoHolder) row.getTag();
        }
        Curso curso = listaCursos.get(posicion);
        holder.txtNombreCurso.setText(curso.getNombre());
        holder.txtPeriodo.setText(curso.getPeriodo());
        return row;
    }
    static class CursoHolder{
        TextView txtNombreCurso;
        TextView txtPeriodo;
    }
}
