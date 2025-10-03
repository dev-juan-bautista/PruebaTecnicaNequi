package co.com.nequi.dynamodb;

import co.com.nequi.dynamodb.entity.ProductEntity;
import co.com.nequi.dynamodb.helper.TemplateAdapterOperations;
import co.com.nequi.dynamodb.mapper.ProductAdapterMapper;
import co.com.nequi.model.gateway.ProductPort;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class ProductTemplateAdapter extends TemplateAdapterOperations<ProductEntity, String, ProductEntity> implements ProductPort {

    private final DynamoDbAsyncTable<ProductEntity> productEntityTable;

    private final ProductAdapterMapper adapterMapper;

    public ProductTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper, ProductAdapterMapper adapterMapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, ProductEntity.class), "producto");
        this.productEntityTable = connectionFactory.table("producto", TableSchema.fromBean(ProductEntity.class));
        this.adapterMapper = adapterMapper;
    }
}
