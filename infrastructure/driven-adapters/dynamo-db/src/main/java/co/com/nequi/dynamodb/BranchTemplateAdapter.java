package co.com.nequi.dynamodb;

import co.com.nequi.dynamodb.entity.BranchEntity;
import co.com.nequi.dynamodb.helper.TemplateAdapterOperations;
import co.com.nequi.dynamodb.mapper.BranchAdapterMapper;
import co.com.nequi.model.Branch;
import co.com.nequi.model.gateway.BranchPort;
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
public class BranchTemplateAdapter extends TemplateAdapterOperations<BranchEntity, String, BranchEntity> implements BranchPort {

    private final DynamoDbAsyncTable<BranchEntity> branchEntityTable;

    private final BranchAdapterMapper adapterMapper;

    public BranchTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper, BranchAdapterMapper adapterMapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, BranchEntity.class), "sucursal");
        this.branchEntityTable = connectionFactory.table("sucursal", TableSchema.fromBean(BranchEntity.class));
        this.adapterMapper = adapterMapper;
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        return Mono.just(branch)
                .doOnNext(f -> {
                    if (f.getId() == null || f.getId().trim().isEmpty()) {
                        f.setId(generateBranchId());
                    }
                })
                .map(adapterMapper::toEntity)
                .flatMap(this::save)
                .map(adapterMapper::toModel)
                .doOnSuccess(saved -> log.info("Sucursal guardada - ID: {}, Nombre: {}",
                        saved.getId(), saved.getName()))
                .doOnError(error -> log.error("Error guardando sucursal: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existByName(String name) {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                .filterExpression(Expression.builder()
                        .expression("nombre = :nombre")
                        .putExpressionValue(":nombre", AttributeValue.builder().s(name).build())
                        .build())
                .limit(1)
                .build();
        return Mono.from(branchEntityTable.scan(scanRequest))
                .map(page -> !page.items().isEmpty())
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Branch> findById(String id) {
        return getById(id)
                .map(adapterMapper::toModel);
    }

    private String generateBranchId() {
        return "SUC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
