package co.com.nequi.dynamodb.mapper;

import co.com.nequi.dynamodb.entity.ProductEntity;
import co.com.nequi.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductAdapterMapper {

    ProductEntity toEntity(Product model);

    Product toModel(ProductEntity entity);

}
