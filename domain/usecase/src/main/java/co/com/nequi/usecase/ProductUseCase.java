package co.com.nequi.usecase;

import co.com.nequi.model.Product;
import co.com.nequi.model.error.BusinessException;
import co.com.nequi.model.gateway.BranchPort;
import co.com.nequi.model.gateway.ProductPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductPort productPort;

    private final BranchPort branchPort;

    public Mono<Product> saveProduct(Product product) {
        return Mono.zip(
                        productPort.existByNameAndBranchId(product.getName(), product.getBranchId()),
                        branchPort.findById(product.getBranchId())
                                .switchIfEmpty(Mono.error(new BusinessException("Sucursal no encontrada con ID: " + product.getBranchId())))
                )
                .flatMap(tuple -> {
                    Boolean exists = tuple.getT1();
                    if (exists) {
                        return Mono.error(new BusinessException(
                                "Ya existe un producto con el nombre: " + product.getName() + " para la sucursal: " + product.getBranchId()
                        ));
                    }
                    return productPort.saveProduct(product);
                });
    }

    public Mono<Product> deleteProduct(Product product) {
        return productPort.findById(product.getId())
                .switchIfEmpty(Mono.error(new BusinessException("Producto no encontrado con ID: " + product.getId())))
                .flatMap(exists -> productPort.deleteById(product.getId())
                        .then(Mono.just(exists))
                );
    }

    public Mono<Product> updateProduct(Product product) {
        return productPort.findById(product.getId())
                .switchIfEmpty(Mono.error(new BusinessException("Producto no encontrado con ID: " + product.getId())))
                .flatMap(existing -> validateNameChange(product, existing))
                .flatMap(validatedExisting -> updateFields(product, validatedExisting))
                .flatMap(productPort::saveProduct);
    }

    private Mono<Product> validateNameChange(Product updatedProduct, Product existingProduct) {
        if (updatedProduct.getName() == null || updatedProduct.getName().trim().isEmpty()) {
            return Mono.just(existingProduct);
        }
        if (existingProduct.getName().equals(updatedProduct.getName())) {
            return Mono.just(existingProduct);
        }
        return productPort.existByNameAndBranchId(updatedProduct.getName(), existingProduct.getBranchId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(
                                "Ya existe un producto con el nombre: " + updatedProduct.getName()
                        ));
                    }
                    return Mono.just(existingProduct);
                });
    }

    private Mono<Product> updateFields(Product updatedProduct, Product existingProduct) {
        if (updatedProduct.getName() != null && !updatedProduct.getName().trim().isEmpty()
                && !existingProduct.getName().equals(updatedProduct.getName())) {
            existingProduct.setName(updatedProduct.getName());
        }
        if (Objects.nonNull(updatedProduct.getStock())) {
            existingProduct.setStock(updatedProduct.getStock());
        }
        return Mono.just(existingProduct);
    }

    public Flux<Product> getMaxStockProduct() {
        return productPort.findByMoreSockInBranches();
    }

}
