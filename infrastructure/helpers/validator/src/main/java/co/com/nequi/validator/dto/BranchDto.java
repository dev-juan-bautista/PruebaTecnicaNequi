package co.com.nequi.validator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchDto {

    private String id;

    @JsonProperty("nombre")
    @NotBlank(message = "El campo nombre es obligatorio")
    @Valid
    private String name;

    @JsonProperty("idFranquicia")
    @NotBlank(message = "El campo idFranquicia es obligatorio")
    @Valid
    private String franchiseId;

}