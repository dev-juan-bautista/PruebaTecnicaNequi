package co.com.nequi.api;

import co.com.nequi.api.util.HandleException;
import co.com.nequi.usecase.BranchUseCase;
import co.com.nequi.usecase.FranchiseUseCase;
import co.com.nequi.validator.dto.BranchDto;
import co.com.nequi.validator.dto.FranchiseDto;
import co.com.nequi.validator.engine.ValidatorEngine;
import co.com.nequi.validator.mapper.BranchMapper;
import co.com.nequi.validator.mapper.FranchiseMapper;
import co.com.nequi.validator.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchUseCase useCase;

    private final ValidatorEngine validatorEngine;

    private final BranchMapper mapper;

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchDto.class)
                .doOnNext(validatorEngine::validate)
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.saveBranch(model)
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, "La franquicia se ha creado con exito"))
                                )
                )
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> updateBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchDto.class)
                .doOnNext(dto -> validatorEngine.validateId(dto.getId()))
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.updateBranch(model)
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
