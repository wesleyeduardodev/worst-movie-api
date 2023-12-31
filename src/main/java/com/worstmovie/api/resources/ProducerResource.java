package com.worstmovie.api.resources;
import com.worstmovie.api.dto.response.ProducerResponseDTO;
import com.worstmovie.api.dto.request.ProducerRequestDTO;
import com.worstmovie.api.model.Producer;
import com.worstmovie.api.service.ProducersService;
import com.worstmovie.api.utils.LinksUtils;
import com.worstmovie.api.utils.ResponseMapperUtils;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import java.util.Optional;

public class ProducerResource implements ProducerResourceAPI {

    @Inject
    ProducersService producersService;

    @Override
    @PermitAll
    public Response findAllProducers(@Context UriInfo uriInfo) {
        return Response.ok(producersService.toProducersResponseDTO(Producer.listAll(), uriInfo.getAbsolutePath().toString())).build();
    }

    @Override
    @PermitAll
    public Response findProducerById(@Context UriInfo uriInfo, @PathParam("id") Long id) {
        Optional<Producer> producer = Producer.findByIdOptional(id);
        Response response;
        if (producer.isPresent()) {
            response = Response.ok(producersService.toProducerResponseDTO(producer.get(), uriInfo.getAbsolutePath().toString())).build();
        } else {
            response = ResponseMapperUtils.notFound();
        }
        return response;
    }

    @Override
    @RolesAllowed("admin")
    public Response saveProducer(@Context UriInfo uriInfo, ProducerRequestDTO producersRequestDTO) {
        Producer producer = producersService.saveProducer(Producer.builder().name(producersRequestDTO.getName()).build());
        ProducerResponseDTO producerResponseDTO = ProducerResponseDTO
                .builder()
                .id(producer.getId())
                .name(producer.getName())
                .links(LinksUtils.generateLinks(uriInfo.getAbsolutePath().toString(), producer.getId()))
                .build();
        return Response.status(RestResponse.Status.CREATED).entity(producerResponseDTO).build();
    }

    @Override
    @RolesAllowed("admin")
    public Response updateProducer(@PathParam("id") Long id, ProducerRequestDTO producersRequestDTO) {
        Optional<Producer> producer = Producer.findByIdOptional(id);
        Response response;
        if (producer.isPresent()) {
            producersService.updateProducer(id, producersRequestDTO);
            response = Response.ok().build();
        } else {
            response = ResponseMapperUtils.badRequest("01", "Producer not found");
        }
        return response;
    }

    @Override
    @Transactional
    @RolesAllowed("admin")
    public Response deleteProducer(@PathParam("id") Long id) {
        Optional<Producer> producer = Producer.findByIdOptional(id);
        Response response;
        if (producer.isPresent()) {
            producersService.deleteProducer(producer.get().getId());
            response = ResponseMapperUtils.noContent();
        } else {
            response = ResponseMapperUtils.notFound();
        }
        return response;
    }
}
