package co.com.nequi.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class ProductEntity {

    private String id;

    private String name;

    private Integer stock;

    private String idBranch;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("nombre")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("stock")
    public Integer getStock() {
        return stock;
    }

    @DynamoDbAttribute("idSucursal")
    public String getIdBranch() {
        return idBranch;
    }
}
