package co.com.nequi.usecase;

import co.com.nequi.model.Branch;
import co.com.nequi.model.constants.BusinessMessages;
import co.com.nequi.model.error.BusinessException;
import co.com.nequi.model.gateway.BranchPort;
import co.com.nequi.model.gateway.FranchisePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchPort branchPort;

    private final FranchisePort franchisePort;

    public Mono<Branch> saveBranch(Branch branch) {
        return Mono.zip(
                        branchPort.existByName(branch.getName()),
                        franchisePort.findById(branch.getFranchiseId())
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        String.format(BusinessMessages.FRANCHISE_NOT_FOUND.getMessage(), branch.getFranchiseId())
                                )))
                )
                .flatMap(tuple -> {
                    Boolean exists = tuple.getT1();
                    if (exists) {
                        return Mono.error(new BusinessException(
                                String.format(BusinessMessages.NAME_ALREADY_EXISTS.getMessage(), branch.getName())
                        ));
                    }
                    return branchPort.saveBranch(branch);
                });
    }

    public Mono<Branch> updateBranch(Branch branch) {
        return branchPort.findById(branch.getId())
                .switchIfEmpty(Mono.error(new BusinessException(
                        String.format(BusinessMessages.DATA_NOT_FOUND.getMessage(), branch.getId())
                )))
                .flatMap(existing ->
                        !existing.getName().equals(branch.getName())
                                ? branchPort.existByName(branch.getName())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new BusinessException(
                                                String.format(BusinessMessages.NAME_ALREADY_EXISTS.getMessage(), branch.getName())
                                        ));
                                    }
                                    existing.setName(branch.getName());
                                    return branchPort.saveBranch(existing);
                                })
                                : Mono.just(existing)
                );
    }

}
