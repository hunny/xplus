package com.example.bootweb.jaxrs.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.bootweb.jaxrs.jersy.Resource;

@Path("/v1.0/resources")
@Produces({ MediaType.APPLICATION_JSON})
public interface ResourceService {
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/hello")
  Map<String,Object> hello();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<Resource> getResources();

  @GET
  @Path("/{id: [0-9]+}")
  @Produces(MediaType.APPLICATION_JSON)
  Resource getResource(@PathParam("id") Long id);

  @POST
  @Consumes({ MediaType.APPLICATION_JSON})
  Response createResource(Resource resource);

  @PUT
  @Path("{id: [0-9]+}")
  @Consumes({ MediaType.APPLICATION_JSON})
  Response updateResource(@PathParam("id") Long id, Resource resource);

  @DELETE
  @Path("{id: [0-9]+}")
  @Consumes({ MediaType.APPLICATION_JSON})
  Response deleteResource(@PathParam("id") Long id);
  
}
