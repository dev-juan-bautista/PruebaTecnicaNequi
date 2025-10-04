package co.com.nequi.usecase;

import co.com.nequi.model.Franchise;
import co.com.nequi.model.constants.BusinessMessages;
import co.com.nequi.model.error.BusinessException;
import co.com.nequi.model.gateway.FranchisePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchisePort franchisePort;

    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return franchisePort.existByName(franchise.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(
                                String.format(BusinessMessages.NAME_ALREADY_EXISTS.getMessage(), franchise.getName())
                        ));
                    }
                    return franchisePort.saveFranchise(franchise);
                });
    }

    public Mono<Franchise> updateFranchise(Franchise franchise) {
        return franchisePort.findById(franchise.getId())
                .switchIfEmpty(Mono.error(new BusinessException(
                        String.format(BusinessMessages.DATA_NOT_FOUND.getMessage(), franchise.getId())
                )))
                .flatMap(existing -> {
                            if (franchise.getName() == null || franchise.getName().trim().isEmpty()) {
                                return Mono.just(existing);
                            }
                            if (existing.getName().equals(franchise.getName())) {
                                return Mono.just(existing);
                            }
                            return franchisePort.existByName(franchise.getName())
                                    .flatMap(exists -> {
                                        if (exists) {
                                            return Mono.error(new BusinessException(
                                                    String.format(BusinessMessages.NAME_ALREADY_EXISTS.getMessage(), franchise.getName())
                                            ));
                                        }
                                        existing.setName(franchise.getName());
                                        return franchisePort.saveFranchise(existing);
                                    });
                        }
                );
    }

}
