package co.com.nequi.validator.mapper;

import co.com.nequi.model.Product;
import co.com.nequi.validator.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "name", expression = "java(dto.getName() != null ? dto.getName().toUpperCase() : null)")
    Product toModel(ProductDto dto);

    ProductDto toDto(Product model);

}
