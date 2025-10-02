package co.com.nequi.usecase;

import co.com.nequi.model.Franchise;
import co.com.nequi.model.gateway.FranchisePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchisePort franchisePort;

    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return Mono.just(franchise);
    }

    public Mono<Franchise> updateFranchise(Franchise franchise) {
        return Mono.just(franchise);
    }

}
