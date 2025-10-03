package co.com.nequi.api;

import co.com.nequi.api.util.HandleException;
import co.com.nequi.usecase.FranchiseUseCase;
import co.com.nequi.validator.dto.FranchiseDto;
import co.com.nequi.validator.engine.ValidatorEngine;
import co.com.nequi.validator.mapper.FranchiseMapper;
import co.com.nequi.validator.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class FranchiseHandler {

    private final FranchiseUseCase useCase;

    private final ValidatorEngine validatorEngine;

    private final FranchiseMapper mapper;

    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseDto.class)
                .doOnNext(dto -> {
                    log.info("REQUEST RECIBIDO >>>>>>>>>> {}", dto.toString());
                    validatorEngine.validate(dto);
                    log.info("VALIDACION EXITOSA");
                })
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.saveFranchise(model)
                                .doOnSuccess(saved -> log.info("EL REGISTRO SE HA REALIZADO CON EXITO!"))
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, "La franquicia se ha creado con exito"))
                                )
                )
                .doOnError(error ->
                        log.error("HA OCURRIDO UN ERROR AL CONSUMIR EL SERVICIO: {}", serverRequest.uri().getPath())
                )
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> updateFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseDto.class)
                .doOnNext(dto -> validatorEngine.validateId(dto.getId()))
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.updateFranchise(model)
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, "La franquicia se ha actualizado con exito"))
                                )
                )
                .onErrorResume(HandleException::handleException);
    }

}