package com.checkm8.auth.users.ms.api.v1.resources;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import com.checkm8.auth.users.ms.entities.User;
import com.checkm8.auth.users.ms.beans.UsersBean;

@ApplicationScoped
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"user", "admin"})
public class UsersResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private UsersBean usersBean;

    @Inject
    private JsonWebToken jwt;

    // ****************************************
    //  GET
    // ****************************************
    @GET
    @RolesAllowed("admin")
    public Response getAll() {

        List<User> users = usersBean.getAll();
        return Response.ok(users).build();
    }

    @GET
    @Path("{sub}")
    @RolesAllowed({"user", "admin"})
    public Response get(@PathParam("sub") String sub) {

        if (!jwt.getSubject().equals(sub) && !jwt.getGroups().contains("admin")) Response.status(Response.Status.FORBIDDEN).build();
        User user = usersBean.get(sub);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(user).build();
    }

    // ****************************************
    //  POST
    // ****************************************
    @POST
    public Response create() {

        usersBean.create(jwt.getSubject());

        return Response.status(Response.Status.CREATED).build();
    }

    // ****************************************
    //  PUT
    // ****************************************
    @PUT
    @Path("{sub}")
    @RolesAllowed({"user", "admin"})
    public Response update(@PathParam("sub") String sub, User user) {

        if (!jwt.getSubject().equals(sub) && !jwt.getGroups().contains("admin")) Response.status(Response.Status.UNAUTHORIZED).build();
        User oldUser = usersBean.get(sub);
        if (oldUser == null) return Response.status(Response.Status.NOT_FOUND).build();

        User updatedUser = usersBean.update(sub, user);
        if (updatedUser == null) return Response.status(Response.Status.NOT_FOUND).build();
        else                     return Response.ok(updatedUser).build();
    }

    // ****************************************
    //  DELETE
    // ****************************************
    @DELETE
    @Path("{sub}")
    @RolesAllowed({"user", "admin"})
    public Response delete(@PathParam("sub") String sub) {

        boolean deleted = usersBean.delete(sub);
        if (!jwt.getSubject().equals(sub) && !jwt.getGroups().contains("admin")) Response.status(Response.Status.UNAUTHORIZED).build();

        if (deleted) return Response.noContent().build();
        else         return Response.status(Response.Status.NOT_FOUND).build();
    }
}
