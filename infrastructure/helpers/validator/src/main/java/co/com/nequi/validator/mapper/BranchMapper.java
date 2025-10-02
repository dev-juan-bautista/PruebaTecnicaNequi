package co.com.nequi.validator.mapper;

import co.com.nequi.model.Branch;
import co.com.nequi.validator.dto.BranchDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toModel(BranchDto dto);

    BranchDto toDto(Branch branch);

}
