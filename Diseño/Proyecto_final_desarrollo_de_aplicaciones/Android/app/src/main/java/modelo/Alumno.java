package modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Desktop on 07/06/2018.
 */

public class Alumno implements Serializable {
    private int idAlumno;
    private String nombre;
    private String correo;
    private String matricula;

    public Alumno(JSONObject jsonAlumno) throws JSONException {
        this.idAlumno = jsonAlumno.getInt("idAlumno");
        this.nombre = jsonAlumno.getString("nombre");
        this.correo = jsonAlumno.getString("correo");
        this.matricula = jsonAlumno.getString("matricula");
    }
    public Alumno(int idAlumno, String nombre, String correo, String matricula) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.correo = correo;
        this.matricula = matricula;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
