package modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Desktop on 06/06/2018.
 */

public class Profesor implements Serializable {
    private int idUsuario;
    private String nombre;
    private String correo;

    public Profesor(int idUsuario, String nombre, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
    }
    public Profesor(JSONObject jsonProfesor) throws JSONException {
        this.idUsuario = jsonProfesor.getInt("idProfesor");
        this.nombre = jsonProfesor.getString("nombre");
        this.correo = jsonProfesor.getString("correo");
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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
}
