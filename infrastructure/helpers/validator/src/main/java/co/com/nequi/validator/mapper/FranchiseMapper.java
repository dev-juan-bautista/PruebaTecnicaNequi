package co.com.nequi.validator.mapper;

import co.com.nequi.model.Franchise;
import co.com.nequi.validator.dto.FranchiseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    Franchise toModel(FranchiseDto dto);

    FranchiseDto toDto(Franchise franchise);

}
