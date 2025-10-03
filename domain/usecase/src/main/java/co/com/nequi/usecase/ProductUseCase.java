package co.com.nequi.usecase;

import co.com.nequi.model.Product;
import co.com.nequi.model.gateway.ProductPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    //private final ProductPort productPort;

    public Mono<Product> saveProduct(Product product) {
        return Mono.just(product);
    }

    public Mono<Product> deleteProduct(Product product) {
        return Mono.just(product);
    }

    public Mono<Product> updateProduct(Product product) {
        return Mono.just(product);
    }

    public Mono<Product> getMaxStockProduct(Product product) {
        return Mono.just(product);
    }

}
