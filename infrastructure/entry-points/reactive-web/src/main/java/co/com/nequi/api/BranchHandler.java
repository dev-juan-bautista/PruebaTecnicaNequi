package co.com.nequi.api;

import co.com.nequi.api.util.HandleException;
import co.com.nequi.usecase.BranchUseCase;
import co.com.nequi.usecase.FranchiseUseCase;
import co.com.nequi.validator.constants.LogMessages;
import co.com.nequi.validator.constants.Responses;
import co.com.nequi.validator.dto.BranchDto;
import co.com.nequi.validator.dto.FranchiseDto;
import co.com.nequi.validator.engine.ValidatorEngine;
import co.com.nequi.validator.mapper.BranchMapper;
import co.com.nequi.validator.mapper.FranchiseMapper;
import co.com.nequi.validator.utils.Logs;
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
public class BranchHandler {

    private final BranchUseCase useCase;

    private final ValidatorEngine validatorEngine;

    private final BranchMapper mapper;

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
        Logs.getLogRequestData(serverRequest);
        return serverRequest.bodyToMono(BranchDto.class)
                .doOnNext(dto -> {
                    log.info(LogMessages.REQUEST_RECEIVED.getMessage(), dto.toString());
                    validatorEngine.validate(dto);
                })
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.saveBranch(model)
                                .doOnSuccess(saved -> {
                                    Logs.getLogRequestData(serverRequest);
                                    log.info(LogMessages.BRANCH_SAVED.getMessage(), saved.toString());
                                })
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, Responses.RESOURCE_CREATED.getMessage()))
                                )
                )
                .doOnError(error -> {
                    Logs.getLogRequestData(serverRequest);
                    log.error(LogMessages.BRANCH_ERROR.getMessage(), error.getMessage());
                })
                .doFinally(signalType -> Logs.clearLogRequestData())
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> updateBranch(ServerRequest serverRequest) {
        Logs.getLogRequestData(serverRequest);
        return serverRequest.bodyToMono(BranchDto.class)
                .doOnNext(dto -> {
                    log.info(LogMessages.REQUEST_RECEIVED.getMessage(), dto.toString());
                    validatorEngine.validateId(dto.getId());
                })
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.updateBranch(model)
                                .doOnSuccess(saved -> {
                                    Logs.getLogRequestData(serverRequest);
                                    log.info(LogMessages.BRANCH_UPDATED.getMessage(), saved.toString());
                                })
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, Responses.RESOURCE_UPDATED.getMessage()))
                                )
                )
                .doOnError(error -> {
                    Logs.getLogRequestData(serverRequest);
                    log.error(LogMessages.BRANCH_ERROR.getMessage(), error.getMessage());
                })
                .doFinally(signalType -> Logs.clearLogRequestData())
                .onErrorResume(HandleException::handleException);
    }

}
