package modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Desktop on 07/06/2018.
 */

public class Actividad implements Serializable {
    private int idActividad;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private int estado;
    private String porcentaje;
    private Curso curso;

    public Actividad(JSONObject jsonActividad) throws JSONException {
        this.idActividad =jsonActividad.getInt("idActividad");
        this.nombre = jsonActividad.getString("nombre");
        this.descripcion = jsonActividad.getString("descripcion");
        this.fechaInicio = jsonActividad.getString("fechaInicio");
        this.fechaFin = jsonActividad.getString("fechaFin");
        this.estado = jsonActividad.getInt("estado");
        this.porcentaje = jsonActividad.getString("porcentaje");
        this.curso = new Curso(jsonActividad.getJSONObject("idCurso"));
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
