package co.com.nequi.model.gateway;

import co.com.nequi.model.Branch;
import reactor.core.publisher.Mono;

public interface BranchPort {

    Mono<Branch> saveBranch(Branch branch);

    Mono<Boolean> existByName(String name);

    Mono<Branch> findById(String id);

}
