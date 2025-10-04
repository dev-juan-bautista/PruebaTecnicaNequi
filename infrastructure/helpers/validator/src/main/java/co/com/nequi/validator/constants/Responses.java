package co.com.nequi.validator.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Responses {

    RESOURCE_CREATED("EL RECURSO SE HA CREADO CON EXITO"),
    RESOURCE_UPDATED("EL RECURSO SE HA MODIFICADO CON EXITO"),
    RESOURCE_DELETE("EL RECURSO SE HA ELIMINADO CON EXITO"),
    QUERY_COMPLETE("SE HA REALIZADO LA CONSULTA CON EXITO");

    private String message;

}
