/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import Modelo.Curso;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Desktop
 */
@Stateless
@Path("modelo.curso")
public class CursoFacadeREST extends AbstractFacade<Curso> {

    @PersistenceContext(unitName = "AccesoBD2018PU")
    private EntityManager em;

    public CursoFacadeREST() {
        super(Curso.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Curso entity) {
        super.create(entity);
    }
    @GET
    @Path("/cursoSolo/{nombre}")//
    @Produces({MediaType.APPLICATION_JSON})
    public Curso buscarCurso(@PathParam("nombre") String  nombre){
        Query query = getEntityManager().createNamedQuery("Curso.findByNombre");
        query.setParameter("nombre", nombre);
        Curso user = (Curso) query.getSingleResult();
        return user;
    }
    @GET
    @Path("/cursosProfesor/{idProfesor}")//
    @Produces({MediaType.APPLICATION_JSON})
    public List<Curso> buscarCursosProfesor(@PathParam("idProfesor") int  idProfesor){
        Query query = getEntityManager().createNamedQuery("Curso.findByIdProfesor");
        query.setParameter("idProfesor", idProfesor);
        List<Curso> user = query.getResultList();
        return user;
    }
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Curso entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Curso find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Curso> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Curso> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
