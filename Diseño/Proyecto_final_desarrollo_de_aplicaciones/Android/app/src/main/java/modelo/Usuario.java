package modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Desktop on 06/06/2018.
 */

public class Usuario implements Serializable {
    private int idUsuario;
    private String usuario;
    private String contrasena;
    private Profesor profesor;
    private transient JSONObject jsonUsuario;

    public Usuario(JSONObject jsonUsuario) throws JSONException {
        idUsuario = jsonUsuario.getInt("idUsuario");
        usuario = jsonUsuario.getString("usuario");
        contrasena = jsonUsuario.getString("contrasena");
        profesor = new Profesor(jsonUsuario.getJSONObject("idProfesor"));
    }
    public Usuario(int idUsuario, String usuario, String contrasena){
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.idUsuario = idUsuario;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

}
