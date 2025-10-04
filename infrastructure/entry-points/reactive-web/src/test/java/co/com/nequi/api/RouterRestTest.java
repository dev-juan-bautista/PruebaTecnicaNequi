package co.com.nequi.api;

import co.com.nequi.validator.dto.FranchiseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class RouterRestTest {

    private WebTestClient webTestClient;

    @Mock
    private FranchiseHandler franchiseHandler;

    @Mock
    private BranchHandler branchHandler;

    @Mock
    private ProductHandler productHandler;

    private RouterRest routerRest;

    @BeforeEach
    void setUp() {
        routerRest = new RouterRest();

        RouterFunction<ServerResponse> franchiseRouter = routerRest.franchiseRouterFunction(franchiseHandler);
        RouterFunction<ServerResponse> branchRouter = routerRest.branchRouterFunction(branchHandler);
        RouterFunction<ServerResponse> productRouter = routerRest.productRouterFunction(productHandler);

        RouterFunction<ServerResponse> combinedRouter = franchiseRouter.and(branchRouter).and(productRouter);

        webTestClient = WebTestClient.bindToRouterFunction(combinedRouter).build();
    }

    @Test
    void franchiseRouterShouldConfigureCreateFranchiseEndpoint() {
        String franchiseRequest = """
        {
            "name": "Test Franchise"
        }
        """;

        FranchiseDto responseDto = new FranchiseDto("1", "Test Franchise");
        when(franchiseHandler.createFranchise(any())).thenReturn(
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseDto)
        );

        webTestClient.post()
                .uri("/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(franchiseRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void franchiseRouterShouldConfigureUpdateFranchiseEndpoint() {
        // Given
        String updateRequest = """
        {
            "id": "1",
            "name": "Updated Franchise"
        }
        """;

        when(franchiseHandler.updateFranchise(any())).thenReturn(
                ServerResponse.accepted().build()
        );

        webTestClient.put()
                .uri("/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    void branchRouterShouldConfigureCreateBranchEndpoint() {
        // Given
        String branchRequest = """
        {
            "name": "Main Branch",
            "franchiseId": "1"
        }
        """;

        when(branchHandler.createBranch(any())).thenReturn(
                ServerResponse.accepted().build()
        );

        webTestClient.post()
                .uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(branchRequest)
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    void branchRouterShouldConfigureUpdateBranchEndpoint() {
        String updateRequest = """
        {
            "id": "1",
            "name": "Updated Branch"
        }
        """;

        when(branchHandler.updateBranch(any())).thenReturn(
                ServerResponse.ok().build()
        );

        webTestClient.put()
                .uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void productRouterShouldConfigureCreateProductEndpoint() {
        String productRequest = """
        {
            "name": "Test Product",
            "stock": 100
        }
        """;

        when(productHandler.createProduct(any())).thenReturn(
                ServerResponse.ok().build()
        );

        webTestClient.post()
                .uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(productRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void productRouterShouldConfigureUpdateStockEndpoint() {
        String updateStockRequest = """
        {
            "id": "1",
            "stock": 50
        }
        """;

        when(productHandler.updateStockProduct(any())).thenReturn(
                ServerResponse.ok().build()
        );

        webTestClient.put()
                .uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updateStockRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void productRouterShouldConfigureGetMaxStockEndpoint() {
        when(productHandler.getMaxStockProduct(any())).thenReturn(
                ServerResponse.ok()
                        .bodyValue("Max stock products")
        );

        webTestClient.get()
                .uri("/productos/max-stock")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Max stock products");
    }

}