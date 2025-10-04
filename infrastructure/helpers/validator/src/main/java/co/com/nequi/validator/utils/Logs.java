package co.com.nequi.validator.utils;

import org.slf4j.MDC;
import org.springframework.web.reactive.function.server.ServerRequest;

public class Logs {

    public static void getLogRequestData(ServerRequest serverRequest) {
        String httpMethod = String.valueOf(serverRequest.method());
        String endpoint = serverRequest.path();
        String traceId = serverRequest.headers().firstHeader("traceId");
        MDC.put("httpMethod", httpMethod);
        MDC.put("endpoint", endpoint);
        MDC.put("traceId", traceId);
    }

    public static void clearLogRequestData(){
        MDC.remove("httpMethod");
        MDC.remove("endpoint");
    }

}
