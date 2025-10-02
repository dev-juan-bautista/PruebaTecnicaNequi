package co.com.nequi.validator.mapper;

import co.com.nequi.model.Product;
import co.com.nequi.validator.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toModel(ProductDto productDto);

    ProductDto toDto(Product product);

}
