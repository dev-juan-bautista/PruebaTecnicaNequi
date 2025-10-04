package co.com.nequi.dynamodb.helper;

import co.com.nequi.dynamodb.entity.FranchiseEntity;
import co.com.nequi.dynamodb.mapper.FranchiseAdapterMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TemplateAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private FranchiseAdapterMapper adapterMapper;

    @Mock
    private DynamoDbAsyncTable<FranchiseEntity> customerTable;

    private FranchiseEntity modelEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("franquicia", TableSchema.fromBean(FranchiseEntity.class)))
                .thenReturn(customerTable);

        modelEntity = new FranchiseEntity();
        modelEntity.setId("id");
        modelEntity.setName("atr1");
    }

    //@Test
    //void modelEntityPropertiesMustNotBeNull() {
    //    FranchiseEntity modelEntityUnderTest = new FranchiseEntity("id", "atr1");

    //    assertNotNull(modelEntityUnderTest.getId());
    //    assertNotNull(modelEntityUnderTest.getName());
    //}

    //@Test
    //void testSave() {
    //    when(customerTable.putItem(modelEntity)).thenReturn(CompletableFuture.runAsync(()->{}));
    //    when(mapper.map(modelEntity, FranchiseEntity.class)).thenReturn(modelEntity);
//
    //    FranchiseTemplateAdapter dynamoDBTemplateAdapter =
    //            new FranchiseTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper, adapterMapper);
//
    //    StepVerifier.create(dynamoDBTemplateAdapter.save(modelEntity))
    //            .expectNextCount(1)
    //            .verifyComplete();
    //}

    //@Test
    //void testGetById() {
    //    String id = "id";
//
    //    when(customerTable.getItem(
    //            Key.builder().partitionValue(AttributeValue.builder().s(id).build()).build()))
    //            .thenReturn(CompletableFuture.completedFuture(modelEntity));
    //    when(mapper.map(modelEntity, FranchiseEntity.class)).thenReturn(new FranchiseEntity());
//
    //    FranchiseTemplateAdapter dynamoDBTemplateAdapter =
    //            new FranchiseTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper, adapterMapper);
//
    //    StepVerifier.create(dynamoDBTemplateAdapter.getById("id"))
    //            .verifyComplete();
    //}
//
    //@Test
    //void testDelete() {
    //    when(mapper.map(modelEntity, FranchiseEntity.class)).thenReturn(modelEntity);
    //    when(mapper.map(modelEntity, FranchiseEntity.class)).thenReturn(new FranchiseEntity());
//
    //    when(customerTable.deleteItem(modelEntity))
    //            .thenReturn(CompletableFuture.completedFuture(modelEntity));
//
    //    FranchiseTemplateAdapter dynamoDBTemplateAdapter =
    //            new FranchiseTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper, adapterMapper);
//
    //    StepVerifier.create(dynamoDBTemplateAdapter.delete(modelEntity))
    //            .verifyComplete();
    //}
}