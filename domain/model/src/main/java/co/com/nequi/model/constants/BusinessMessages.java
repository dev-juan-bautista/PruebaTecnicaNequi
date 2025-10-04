package co.com.nequi.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BusinessMessages {

    NAME_ALREADY_EXISTS("EL NOMBRE %s YA HA SIDO REGISTRADO"),
    PRODUCT_BRANCH_NAME_ALREADY_EXISTS("YA EXISTE UN PRODUCTO CON EL NOMBRE %s PARA LA SUCRUSAL %s"),
    DATA_NOT_FOUND("NO SE HA ENCONTRADO EL REGISTRO CON EL ID %s"),
    FRANCHISE_NOT_FOUND("NO SE HA ENCONTRADO LA FRANQUICIA CON EL ID %s"),
    BRANCH_NOT_FOUND("NO SE HA ENCONTRADO LA SUCURSAL CON EL ID %s");

    private String message;

}
