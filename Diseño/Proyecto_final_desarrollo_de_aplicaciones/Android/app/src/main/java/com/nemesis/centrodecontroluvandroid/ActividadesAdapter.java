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

import modelo.Actividad;

/**
 * Created by Desktop on 07/06/2018.
 */

public class ActividadesAdapter extends ArrayAdapter<Actividad> {
    private Context context;
    private int resource;
    private List<Actividad> listaActividades;
    public ActividadesAdapter(@NonNull Context context, int resource, List<Actividad> listaActividades) {
        super(context, resource, listaActividades);
        this.context = context;
        this.resource = resource;
        this.listaActividades = listaActividades;
    }
    public View getView(int posicion, View convertView, ViewGroup parent){
        View row = convertView;
        ActividadesAdapter.ActividadHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource,parent,false);
            holder = new ActividadesAdapter.ActividadHolder();
            holder.txtNombreActividad = (TextView) row.findViewById(R.id.txtNombreActividad);
            holder.txtEstadoActividad = (TextView) row.findViewById(R.id.txtEstadoActividad);
            holder.txtPorcentajeActividad = (TextView) row.findViewById(R.id.txtPorcentajeActividad);
            row.setTag(holder);
        }else{
            holder = (ActividadesAdapter.ActividadHolder) row.getTag();
        }
        Actividad actividad = listaActividades.get(posicion);
        holder.txtNombreActividad.setText(actividad.getNombre());
        if(actividad.getEstado() == 0){
            holder.txtEstadoActividad.setText("Pendiente");
        }else{
            holder.txtEstadoActividad.setText("Revisado");
        }
        holder.txtPorcentajeActividad.setText(actividad.getPorcentaje().toString()+" puntos");
        return row;
    }
    static class ActividadHolder{
        TextView txtNombreActividad;
        TextView txtEstadoActividad;
        TextView txtPorcentajeActividad;
    }
}