package co.com.nequi.dynamodb.mapper;

import co.com.nequi.dynamodb.entity.FranchiseEntity;
import co.com.nequi.model.Franchise;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FranchiseAdapterMapper {

    FranchiseEntity toEntity(Franchise model);

    Franchise toModel(FranchiseEntity entity);

}
