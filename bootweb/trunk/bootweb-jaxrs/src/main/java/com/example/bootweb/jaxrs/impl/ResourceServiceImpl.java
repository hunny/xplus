package com.example.bootweb.jaxrs.impl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.example.bootweb.jaxrs.api.ResourceService;
import com.example.bootweb.jaxrs.jersy.Resource;

@Component
public class ResourceServiceImpl implements ResourceService {

  private static List<Resource> resources = null;

  static {
    resources = new ArrayList<>();
    resources.add(new Resource(1L, "Resource One", LocalDateTime.now(), null));
    resources.add(new Resource(2L, "Resource Two", LocalDateTime.now(), null));
    resources.add(new Resource(3L, "Resource Three", LocalDateTime.now(), null));
    resources.add(new Resource(4L, "Resource Four", LocalDateTime.now(), null));
    resources.add(new Resource(5L, "Resource Five", LocalDateTime.now(), null));
    resources.add(new Resource(6L, "Resource Six", LocalDateTime.now(), null));
    resources.add(new Resource(7L, "Resource Seven", LocalDateTime.now(), null));
    resources.add(new Resource(8L, "Resource Eight", LocalDateTime.now(), null));
    resources.add(new Resource(9L, "Resource Nine", LocalDateTime.now(), null));
    resources.add(new Resource(10L, "Resource Ten", LocalDateTime.now(), null));
  }

  @Override
  public Map<String, Object> hello() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("code", "1");
    map.put("codeMsg", "success");
    return map;
  }

  @Override
  public List<Resource> getResources() {
    return resources;
  }

  @Override
  public Resource getResource(Long id) {
    Resource resource = new Resource(id, null, null, null);

    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

    if (index >= 0)
      return resources.get(index);
    else
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }

  @Override
  public Response createResource(Resource resource) {
    if (Objects.isNull(resource.getId()))
      throw new WebApplicationException(Response.Status.BAD_REQUEST);

    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

    if (index < 0) {
      resource.setCreatedTime(LocalDateTime.now());
      resources.add(resource);
      return Response.status(Response.Status.CREATED)
          .location(URI.create(String.format("/api/v1.0/resources/%s", resource.getId()))).build();
    } else
      throw new WebApplicationException(Response.Status.CONFLICT);
  }

  @Override
  public Response updateResource(Long id, Resource resource) {
    resource.setId(id);
    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

    if (index >= 0) {
      Resource updatedResource = resources.get(index);
      updatedResource.setModifiedTime(LocalDateTime.now());
      updatedResource.setDescription(resource.getDescription());
      resources.set(index, updatedResource);
      return Response.status(Response.Status.NO_CONTENT).build();
    } else
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }

  @Override
  public Response deleteResource(Long id) {
    Resource resource = new Resource(id, null, null, null);
    int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));
    if (index >= 0) {
      resources.remove(index);
      return Response.status(Response.Status.NO_CONTENT).build();
    } else
      throw new WebApplicationException(Response.Status.NOT_FOUND);
  }
}
