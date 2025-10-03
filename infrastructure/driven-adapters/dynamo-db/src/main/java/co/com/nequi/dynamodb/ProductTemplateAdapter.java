package co.com.nequi.dynamodb;

import co.com.nequi.dynamodb.entity.ProductEntity;
import co.com.nequi.dynamodb.helper.TemplateAdapterOperations;
import co.com.nequi.dynamodb.mapper.ProductAdapterMapper;
import co.com.nequi.model.Product;
import co.com.nequi.model.gateway.ProductPort;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ProductTemplateAdapter extends TemplateAdapterOperations<ProductEntity, String, ProductEntity> implements ProductPort {

    private final DynamoDbAsyncTable<ProductEntity> productEntityTable;

    private final ProductAdapterMapper adapterMapper;

    public ProductTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper, ProductAdapterMapper adapterMapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, ProductEntity.class), "producto");
        this.productEntityTable = connectionFactory.table("producto", TableSchema.fromBean(ProductEntity.class));
        this.adapterMapper = adapterMapper;
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return Mono.just(product)
                .doOnNext(f -> {
                    if (f.getId() == null || f.getId().trim().isEmpty()) {
                        f.setId(generateProductId());
                    }
                })
                .map(adapterMapper::toEntity)
                .flatMap(this::save)
                .map(adapterMapper::toModel)
                .doOnSuccess(saved -> log.info("Producto guardada - ID: {}, Nombre: {}",
                        saved.getId(), saved.getName()))
                .doOnError(error -> log.error("Error guardando producto: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existByNameAndBranchId(String name, String branchId) {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                .filterExpression(Expression.builder()
                        .expression("nombre = :nombre AND idSucursal = :idSucursal")
                        .putExpressionValue(":nombre", AttributeValue.builder().s(name).build())
                        .putExpressionValue(":idSucursal", AttributeValue.builder().s(branchId).build())
                        .build())
                .build();
        return Mono.from(productEntityTable.scan(scanRequest))
                .map(page -> !page.items().isEmpty())
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Product> findById(String id) {
        return getById(id)
                .map(adapterMapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return Mono.fromCallable(() -> {
                    ProductEntity entity = new ProductEntity();
                    entity.setId(id);
                    return entity;
                })
                .flatMap(this::delete)
                .then();
    }

    @Override
    public Flux<Product> findByMoreSockInBranches() {
        return Flux.from(productEntityTable.scan())
                .flatMapIterable(Page::items)
                .collectList()
                .flatMapMany(allProducts -> {
                    if (allProducts.isEmpty()) {
                        return Flux.empty();
                    }
                    Map<String, Optional<ProductEntity>> maxStockByBranch = allProducts.stream()
                            .filter(product -> product.getBranchId() != null && product.getStock() != null)
                            .collect(Collectors.groupingBy(
                                    ProductEntity::getBranchId,
                                    Collectors.maxBy(Comparator.comparing(ProductEntity::getStock))
                            ));
                    List<ProductEntity> result = maxStockByBranch.values().stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    return Flux.fromIterable(result)
                            .map(adapterMapper::toModel);
                })
                .doOnError(error -> log.error("Error en consulta global: {}", error.getMessage()));
    }

    private String generateProductId() {
        return "PROD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
