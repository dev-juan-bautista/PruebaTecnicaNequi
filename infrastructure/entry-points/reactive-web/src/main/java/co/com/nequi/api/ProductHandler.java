package co.com.nequi.api;

import co.com.nequi.api.util.HandleException;
import co.com.nequi.usecase.ProductUseCase;
import co.com.nequi.validator.constants.LogMessages;
import co.com.nequi.validator.constants.Responses;
import co.com.nequi.validator.dto.ProductDto;
import co.com.nequi.validator.engine.ValidatorEngine;
import co.com.nequi.validator.mapper.ProductMapper;
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
public class ProductHandler {

    private final ProductUseCase useCase;

    private final ValidatorEngine validatorEngine;

    private final ProductMapper mapper;

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        Logs.getLogRequestData(serverRequest);
        return serverRequest.bodyToMono(ProductDto.class)
                .doOnNext(dto -> {
                    log.info(LogMessages.REQUEST_RECEIVED.getMessage(), dto.toString());
                    validatorEngine.validate(dto);
                })
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.saveProduct(model)
                                .doOnSuccess(saved -> {
                                    Logs.getLogRequestData(serverRequest);
                                    log.info(LogMessages.PRODUCT_SAVED.getMessage(), saved.toString());
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
                    log.error(LogMessages.PRODUCT_ERROR.getMessage(), error.getMessage());
                })
                .doFinally(signalType -> Logs.clearLogRequestData())
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        Logs.getLogRequestData(serverRequest);
        return serverRequest.bodyToMono(ProductDto.class)
                .doOnNext(dto -> {
                    log.info(LogMessages.REQUEST_RECEIVED.getMessage(), dto.toString());
                    validatorEngine.validate(dto);
                })
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.deleteProduct(model)
                                .doOnSuccess(saved -> {
                                    Logs.getLogRequestData(serverRequest);
                                    log.info(LogMessages.PRODUCT_DELETE.getMessage(), saved.toString());
                                })
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, Responses.RESOURCE_DELETE.getMessage()))
                                )
                )
                .doOnError(error -> {
                    Logs.getLogRequestData(serverRequest);
                    log.error(LogMessages.PRODUCT_ERROR.getMessage(), error.getMessage());
                })
                .doFinally(signalType -> Logs.clearLogRequestData())
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> updateStockProduct(ServerRequest serverRequest) {
        Logs.getLogRequestData(serverRequest);
        return serverRequest.bodyToMono(ProductDto.class)
                .doOnNext(dto -> {
                    log.info(LogMessages.REQUEST_RECEIVED.getMessage(), dto.toString());
                    validatorEngine.validateId(dto.getId());
                })
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.updateProduct(model)
                                .doOnSuccess(saved -> {
                                    Logs.getLogRequestData(serverRequest);
                                    log.info(LogMessages.PRODUCT_UPDATED.getMessage(), saved.toString());
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
                    log.error(LogMessages.PRODUCT_ERROR.getMessage(), error.getMessage());
                })
                .doFinally(signalType -> Logs.clearLogRequestData())
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> getMaxStockProduct(ServerRequest serverRequest) {
        return useCase.getMaxStockProduct()
                .map(mapper::toDto)
                .collectList()
                .flatMap(dto ->
                        ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, Responses.QUERY_COMPLETE.getMessage()))
                )
                .doOnError(error -> {
                    Logs.getLogRequestData(serverRequest);
                    log.error(LogMessages.PRODUCT_ERROR.getMessage(), error.getMessage());
                })
                .doFinally(signalType -> Logs.clearLogRequestData())
                .onErrorResume(HandleException::handleException);
    }

}
