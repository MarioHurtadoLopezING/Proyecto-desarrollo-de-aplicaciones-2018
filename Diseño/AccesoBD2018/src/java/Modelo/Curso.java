/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desktop
 */
@Entity
@Table(name = "curso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c")
    , @NamedQuery(name = "Curso.findByIdCurso", query = "SELECT c FROM Curso c WHERE c.idCurso = :idCurso")
    , @NamedQuery(name = "Curso.findByNombre", query = "SELECT c FROM Curso c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Curso.findByPeriodo", query = "SELECT c FROM Curso c WHERE c.periodo = :periodo")})
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCurso")
    private Integer idCurso;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "periodo")
    private String periodo;
    @OneToMany(mappedBy = "idCurso")
    private Collection<Inscripcion> inscripcionCollection;
    @OneToMany(mappedBy = "idCurso")
    private Collection<Asistencia> asistenciaCollection;
    @JoinColumn(name = "idProfesor", referencedColumnName = "idProfesor")
    @ManyToOne
    private Profesor idProfesor;
    @OneToMany(mappedBy = "idCurso")//persistencia en cascada @OneToMany(mappedBy = "idCurso", cascade = CascadeType.PERSIST)
    private Collection<Registroactividad> registroactividadCollection;
    @OneToMany(mappedBy = "idCurso")
    private Collection<Dia> diaCollection;
    @OneToMany(mappedBy = "idCurso")
    private Collection<Actividad> actividadCollection;

    public Curso() {
    }

    public Curso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
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

    @XmlTransient
    public Collection<Inscripcion> getInscripcionCollection() {
        return inscripcionCollection;
    }

    public void setInscripcionCollection(Collection<Inscripcion> inscripcionCollection) {
        this.inscripcionCollection = inscripcionCollection;
    }

    @XmlTransient
    public Collection<Asistencia> getAsistenciaCollection() {
        return asistenciaCollection;
    }

    public void setAsistenciaCollection(Collection<Asistencia> asistenciaCollection) {
        this.asistenciaCollection = asistenciaCollection;
    }

    public Profesor getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Profesor idProfesor) {
        this.idProfesor = idProfesor;
    }

    @XmlTransient
    public Collection<Registroactividad> getRegistroactividadCollection() {
        return registroactividadCollection;
    }

    public void setRegistroactividadCollection(Collection<Registroactividad> registroactividadCollection) {
        this.registroactividadCollection = registroactividadCollection;
    }

    @XmlTransient
    public Collection<Dia> getDiaCollection() {
        return diaCollection;
    }

    public void setDiaCollection(Collection<Dia> diaCollection) {
        this.diaCollection = diaCollection;
    }

    @XmlTransient
    public Collection<Actividad> getActividadCollection() {
        return actividadCollection;
    }

    public void setActividadCollection(Collection<Actividad> actividadCollection) {
        this.actividadCollection = actividadCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCurso != null ? idCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.idCurso == null && other.idCurso != null) || (this.idCurso != null && !this.idCurso.equals(other.idCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Curso[ idCurso=" + idCurso + " ]";
    }
    
}
