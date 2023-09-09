package com.worstmovie.api.resources;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;;

@Path("/v1/awardsrange")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Awards Range Resource ", description = "Routes used to return Awards Range.")
public interface AwardsRangeResourceAPI {

    @Operation(
            description = "Essa rota serve para obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que\n" +
                    "obteve dois prêmios mais rápido.",
            operationId = "awardsRangeResource.findAwardsRangeProducer",
            summary = "Obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que\n" +
                    "obteve dois prêmios mais rápido."
    )
    @APIResponse(
            name = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            ref = "MaxMinAwardsRangeResponseDTO"
                    )
            ),
            description = "Request executed successfully."
    )
    @GET
    @Path("/producers")
    @APIResponse(responseCode = "401", ref = "unauthorized")
    @APIResponse(responseCode = "403", ref = "forbiden")
    @APIResponse(responseCode = "500", ref = "internalError")
    Response findAwardsRangeProducer();

    @Operation(
            description = "Essa rota serve apenas para indicar uma possível evolução futura da API. Onde seja possível também retornar\n" +
                    "Studio com maior intervalo entre dois prêmios consecutivos. No momento essa rota não retornará dados.",
            operationId = "awardsRangeResource.findAwardsRangeStudios",
            summary = "Return Awards Range Studios.."
    )
    @APIResponse(
            name = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            ref = "MaxMinAwardsRangeResponseDTO"
                    )
            ),
            description = "Request executed successfully."
    )
    @GET
    @Path("/studios")
    @APIResponse(responseCode = "401", ref = "unauthorized")
    @APIResponse(responseCode = "403", ref = "forbiden")
    @APIResponse(responseCode = "500", ref = "internalError")
        //TODO Essa rota serve apenas para indicar uma possível evolução futura da API. Onde seja possível também retornar
        // o Studio com maior intervalo entre dois prêmios consecutivos. No momento essa rota não retornará dados.
    Response findAwardsRangeStudios();
}
