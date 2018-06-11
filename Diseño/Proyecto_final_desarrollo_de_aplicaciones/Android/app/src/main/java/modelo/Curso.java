package modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Desktop on 07/06/2018.
 */

public class Curso implements Serializable {
    private int idCurso;
    private String nombre;
    private String periodo;
    private Profesor profesor;

    public Curso(JSONObject jsonCurso) throws JSONException {
        idCurso = jsonCurso.getInt("idCurso");
        nombre = jsonCurso.getString("nombre");
        periodo = jsonCurso.getString("periodo");
        profesor = new Profesor(jsonCurso.getJSONObject("idProfesor"));
    }
    public Curso(int idCurso, String nombre, String periodo, Profesor profesor) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.periodo = periodo;
        this.profesor = profesor;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
}

