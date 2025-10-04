package co.com.nequi.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class RouterRest {

    private static final String FRANCHISE_PATH = "/franquicias";
    private static final String BRANCH_PATH = "/sucursales";
    private static final String PRODUCT_PATH = "/productos";

    @Bean
    public RouterFunction<ServerResponse> franchiseRouterFunction(FranchiseHandler handler) {
        return RouterFunctions.route()
                .path(FRANCHISE_PATH, builder -> builder
                        .POST("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::createFranchise)
                        .PUT("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::updateFranchise)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> branchRouterFunction(BranchHandler handler) {
        return RouterFunctions.route()
                .path(BRANCH_PATH, builder -> builder
                        .POST("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::createBranch)
                        .PUT("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::updateBranch)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productRouterFunction(ProductHandler handler) {
        return RouterFunctions.route()
                .path(PRODUCT_PATH, builder -> builder
                        .POST("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::createProduct)
                        .DELETE("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::deleteProduct)
                        .PUT("",
                                accept(MediaType.APPLICATION_JSON)
                                        .and(contentType(MediaType.APPLICATION_JSON)),
                                handler::updateStockProduct)
                        .GET("/max-stock",
                                handler::getMaxStockProduct)
                )
                .build();
    }

}