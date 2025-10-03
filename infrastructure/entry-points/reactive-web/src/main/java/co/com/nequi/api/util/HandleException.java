package co.com.nequi.api.util;

import co.com.nequi.model.error.BusinessException;
import co.com.nequi.validator.error.ValidationException;
import co.com.nequi.validator.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class HandleException {

    public static Mono<ServerResponse> handleException(Throwable ex) {
        if (ex instanceof ValidationException validationException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST.value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ResponseBuilder.buildValidationErrorResponse(validationException));
        } else if (ex instanceof BusinessException businessException) {
          return ServerResponse.status(HttpStatus.NOT_ACCEPTABLE.value())
                  .contentType(MediaType.APPLICATION_JSON)
                  .bodyValue(ResponseBuilder.buildBusinessErrorResponse(businessException));
          }
        else {
            return ServerResponse.status(HttpStatus.NOT_ACCEPTABLE.value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ResponseBuilder.buildGenericErrorResponse(ex));
        }
    }

}
