package co.com.nequi.validator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductDto {

    private String id;

    @JsonProperty("nombre")
    @NotBlank(message = "El campo nombre es obligatorio")
    @Valid
    private String name;

    @JsonProperty("cantidad")
    @NotNull(message = "El campo cantidad no puede ser nulo")
    @Valid
    private Integer stock;

    @JsonProperty("idSucursal")
    @NotBlank(message = "El campo idSucursal es obligatorio")
    @Valid
    private String idBranch;

}
