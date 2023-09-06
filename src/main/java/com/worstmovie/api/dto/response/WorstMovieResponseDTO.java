package com.worstmovie.api.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Object used to map WorstMovie datas.",
        name = "WorstMovieResponseDTO",
        type = SchemaType.OBJECT
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WorstMovieResponseDTO {

    @Schema(
            description = "WorstMovie code.",
            implementation = Long.class,
            type = SchemaType.NUMBER
    )
    private Long id;

    @Schema(
            description = "WorstMovie year.",
            implementation = Integer.class,
            type = SchemaType.INTEGER
    )
    private Integer year;

    @Schema(
            description = "WorstMovie title.",
            implementation = String.class,
            type = SchemaType.STRING
    )
    private String title;

    @Schema(
            description = "WorstMovie winner.",
            implementation = Boolean.class,
            type = SchemaType.BOOLEAN
    )
    private boolean winner;
}