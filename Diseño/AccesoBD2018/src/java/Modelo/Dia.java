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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desktop
 */
@Entity
@Table(name = "dia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dia.findAll", query = "SELECT d FROM Dia d")
    , @NamedQuery(name = "Dia.findByIdDia", query = "SELECT d FROM Dia d WHERE d.idDia = :idDia")
    , @NamedQuery(name = "Dia.findByNombre", query = "SELECT d FROM Dia d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Dia.findByHoraInicio", query = "SELECT d FROM Dia d WHERE d.horaInicio = :horaInicio")
    , @NamedQuery(name = "Dia.findByHoraFin", query = "SELECT d FROM Dia d WHERE d.horaFin = :horaFin")
    , @NamedQuery(name = "Dia.findBySalon", query = "SELECT d FROM Dia d WHERE d.salon = :salon")})
public class Dia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDia")
    private Integer idDia;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "horaInicio")
    private String horaInicio;
    @Size(max = 50)
    @Column(name = "horaFin")
    private String horaFin;
    @Size(max = 4)
    @Column(name = "salon")
    private String salon;
    @JoinColumn(name = "idCurso", referencedColumnName = "idCurso")
    @ManyToOne
    private Curso idCurso;
    @JoinColumn(name = "idProfesor", referencedColumnName = "idProfesor")
    @ManyToOne
    private Profesor idProfesor;

    public Dia() {
    }

    public Dia(Integer idDia) {
        this.idDia = idDia;
    }

    public Integer getIdDia() {
        return idDia;
    }

    public void setIdDia(Integer idDia) {
        this.idDia = idDia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    public Profesor getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Profesor idProfesor) {
        this.idProfesor = idProfesor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDia != null ? idDia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dia)) {
            return false;
        }
        Dia other = (Dia) object;
        if ((this.idDia == null && other.idDia != null) || (this.idDia != null && !this.idDia.equals(other.idDia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Dia[ idDia=" + idDia + " ]";
    }
    
}
