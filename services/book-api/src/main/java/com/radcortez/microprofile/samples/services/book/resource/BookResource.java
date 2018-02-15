package com.radcortez.microprofile.samples.services.book.resource;

import com.radcortez.microprofile.samples.services.book.entity.Book;
import com.radcortez.microprofile.samples.services.book.persistence.BookBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@ApplicationScoped
@Path("books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class BookResource {
    @Inject
    private BookBean bookBean;

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        return bookBean.findById(id)
                       .map(Response::ok)
                       .orElse(status(NOT_FOUND))
                       .build();
    }

    @GET
    public Response findAll() {
        return ok(bookBean.findAll()).build();
    }

    @POST
    public Response create(final Book book, @Context UriInfo uriInfo) {
        final Book created = bookBean.create(book);
        final URI createdURI = uriInfo.getBaseUriBuilder()
                                      .path("books/{id}")
                                      .resolveTemplate("id", created.getId())
                                      .build();
        return Response.created(createdURI).build();
    }

    @PUT
    public Response update(final Book book) {
        return ok(bookBean.update(book)).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response delete(@PathParam("id") final Long id) {
        bookBean.deleteById(id);
        return noContent().build();
    }
}
