package co.com.nequi.validator.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiGenericResponse<T> {

    @JsonProperty(value = "resultado")
    private boolean success;

    @JsonProperty(value = "datos")
    private T data;

    @JsonProperty(value = "errores")
    private List<Map<String, String>> errors;

    @JsonProperty(value = "codigoRespuesta")
    private String responseCode;

    @JsonProperty(value = "mensaje")
    private String message;

    @JsonProperty(value = "fechaHora")
    private LocalDateTime timestamp;

}
