package co.com.nequi.usecase;

import co.com.nequi.model.Branch;
import co.com.nequi.model.gateway.BranchPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchPort branchPort;

    public Mono<Branch> saveBranch(Branch branch) {
        return Mono.just(branch);
    }

    public Mono<Branch> updateBranch(Branch branch) {
        return Mono.just(branch);
    }

}
