package co.com.nequi.api;

import co.com.nequi.api.util.HandleException;
import co.com.nequi.usecase.ProductUseCase;
import co.com.nequi.validator.dto.ProductDto;
import co.com.nequi.validator.engine.ValidatorEngine;
import co.com.nequi.validator.mapper.ProductMapper;
import co.com.nequi.validator.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase useCase;

    private final ValidatorEngine validatorEngine;

    private final ProductMapper mapper;

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProductDto.class)
                .doOnNext(validatorEngine::validate)
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.saveProduct(model)
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, "El producto se ha creado con exito"))
                                )
                )
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProductDto.class)
                .doOnNext(dto -> validatorEngine.validateId(dto.getId()))
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.deleteProduct(model)
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, "El producto se ha eliminado con exito"))
                                )
                )
                .onErrorResume(HandleException::handleException);
    }

    public Mono<ServerResponse> updateStockProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ProductDto.class)
                .doOnNext(dto -> validatorEngine.validateId(dto.getId()))
                .map(mapper::toModel)
                .flatMap(model ->
                        useCase.updateProduct(model)
                                .map(mapper::toDto)
                                .flatMap(dto ->
                                        ServerResponse.accepted()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, "El producto se ha actualizado con exito"))
                                )
                )
                .onErrorResume(HandleException::handleException);
    }

    //AJUSTAR
    public Mono<ServerResponse> getMaxStockProduct(ServerRequest serverRequest) {
        return useCase.getMaxStockProduct()
                .map(mapper::toDto)
                .collectList()
                .flatMap(dto ->
                        ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(ResponseBuilder.buildSuccessResponse(dto, "Se ha realizado la consulta con exito"))
                )

                .onErrorResume(HandleException::handleException);
    }

}
