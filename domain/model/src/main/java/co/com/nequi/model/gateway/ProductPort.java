package co.com.nequi.model.gateway;

import co.com.nequi.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPort {

    Mono<Product> saveProduct(Product product);

    Mono<Boolean> existByNameAndBranchId(String name, String branchId);

    Mono<Product> findById(String id);

    Mono<Void> deleteById(String id);

    Flux<Product> findByMoreSockInBranches();

}
