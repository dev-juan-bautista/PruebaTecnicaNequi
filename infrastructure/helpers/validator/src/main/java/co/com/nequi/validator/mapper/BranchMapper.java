package co.com.nequi.validator.mapper;

import co.com.nequi.model.Branch;
import co.com.nequi.validator.dto.BranchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(target = "name", expression = "java(dto.getName() != null ? dto.getName().toUpperCase() : null)")
    Branch toModel(BranchDto dto);

    BranchDto toDto(Branch model);

}
