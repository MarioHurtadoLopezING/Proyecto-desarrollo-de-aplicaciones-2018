/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desktop
 */
@Entity
@Table(name = "registroactividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registroactividad.findAll", query = "SELECT r FROM Registroactividad r")
    , @NamedQuery(name = "Registroactividad.findByIdActividad", query = "SELECT r FROM Registroactividad r WHERE r.idActividad = :idActividad")
    , @NamedQuery(name = "Registroactividad.findByPorcentaje", query = "SELECT r FROM Registroactividad r WHERE r.porcentaje = :porcentaje")})
public class Registroactividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idActividad")
    private Integer idActividad;
    @Column(name = "porcentaje")
    private Integer porcentaje;
    @JoinColumn(name = "idCurso", referencedColumnName = "idCurso")
    @ManyToOne
    private Curso idCurso;
    @JoinColumn(name = "idAlumno", referencedColumnName = "idAlumno")
    @ManyToOne
    private Alumno idAlumno;

    public Registroactividad() {
    }

    public Registroactividad(Integer idActividad) {
        this.idActividad = idActividad;
    }

    public Integer getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Integer idActividad) {
        this.idActividad = idActividad;
    }

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    public Alumno getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Alumno idAlumno) {
        this.idAlumno = idAlumno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActividad != null ? idActividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Registroactividad)) {
            return false;
        }
        Registroactividad other = (Registroactividad) object;
        if ((this.idActividad == null && other.idActividad != null) || (this.idActividad != null && !this.idActividad.equals(other.idActividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Registroactividad[ idActividad=" + idActividad + " ]";
    }
    
}
