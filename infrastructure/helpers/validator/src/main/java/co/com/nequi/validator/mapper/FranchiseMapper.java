package co.com.nequi.validator.mapper;

import co.com.nequi.model.Franchise;
import co.com.nequi.validator.dto.FranchiseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    @Mapping(target = "name", expression = "java(dto.getName() != null ? dto.getName().toUpperCase() : null)")
    Franchise toModel(FranchiseDto dto);

    FranchiseDto toDto(Franchise model);

}
