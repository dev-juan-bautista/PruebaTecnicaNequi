package co.com.nequi.dynamodb;

import co.com.nequi.dynamodb.entity.FranchiseEntity;
import co.com.nequi.dynamodb.helper.TemplateAdapterOperations;
import co.com.nequi.dynamodb.mapper.FranchiseAdapterMapper;
import co.com.nequi.model.Franchise;
import co.com.nequi.model.gateway.FranchisePort;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.UUID;

@Repository
@Slf4j
public class FranchiseTemplateAdapter extends TemplateAdapterOperations<FranchiseEntity, String, FranchiseEntity> implements FranchisePort {

    private final DynamoDbAsyncTable<FranchiseEntity> franchiseEntityTable;

    private final FranchiseAdapterMapper adapterMapper;

    public FranchiseTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper, FranchiseAdapterMapper adapterMapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, FranchiseEntity.class), "franquicia");
        this.franchiseEntityTable = connectionFactory.table("franquicia", TableSchema.fromBean(FranchiseEntity.class));
        this.adapterMapper = adapterMapper;
    }

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return Mono.just(franchise)
                .doOnNext(f -> {
                    if (f.getId() == null || f.getId().trim().isEmpty()) {
                        f.setId(generateFranchiseId());
                    }
                })
                .map(adapterMapper::toEntity)
                .flatMap(this::save)
                .map(adapterMapper::toModel)
                .doOnSuccess(saved -> log.info("Franquicia guardada - ID: {}, Nombre: {}",
                        saved.getId(), saved.getName()))
                .doOnError(error -> log.error("Error guardando franquicia: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existByName(String name) {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                .filterExpression(Expression.builder()
                        .expression("nombre = :nombre")
                        .putExpressionValue(":nombre", AttributeValue.builder().s(name).build())
                        .build())
                .build();
        return Mono.from(franchiseEntityTable.scan(scanRequest))
                .map(page -> !page.items().isEmpty())
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return getById(id)
                .map(adapterMapper::toModel);
    }

    private String generateFranchiseId() {
        return "FRQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
