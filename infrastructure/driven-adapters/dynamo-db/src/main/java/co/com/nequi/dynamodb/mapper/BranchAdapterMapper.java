package co.com.nequi.dynamodb.mapper;

import co.com.nequi.dynamodb.entity.BranchEntity;
import co.com.nequi.model.Branch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchAdapterMapper {

    BranchEntity toEntity(Branch model);

    Branch toModel(BranchEntity entity);

}
