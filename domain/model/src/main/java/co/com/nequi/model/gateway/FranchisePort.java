package co.com.nequi.model.gateway;

import co.com.nequi.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePort {

    Mono<Franchise> saveFranchise(Franchise franchise);

    Mono<Boolean> existByName(String name);

    Mono<Franchise> findById(String id);

}
