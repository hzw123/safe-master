package cn.mauth.safe.gateway.config;
import cn.mauth.safe.gateway.filter.SingleGatewayFilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class RouteConfiguration {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    //这里为支持的请求头，如果有自定义的header字段请自己添加（不知道为什么不能使用*）
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_Expose = "*";
    private static final String MAX_AGE = "18000L";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                logger.info("\n\t==================================" +
                        "\n\t--自定义的header-----"+
                        "\n\t==================================");
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                headers.add("Access-Control-Expose-Headers", ALLOWED_Expose);
                headers.add("Access-Control-Allow-Credentials", "true");
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        logger.info("====================ipKeyResolver=====================");
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    @Bean
    public SingleGatewayFilterFactory singleGatewayFilterFactory(){
        return new SingleGatewayFilterFactory();
    }

    /**
     *
     *如果使用了注册中心（如：Eureka），进行控制则需要增加如下配置
     */
    @Bean
    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient,
                                                                        DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient,properties);
    }

    @Configuration
    @ConditionalOnClass(Health.class)
    protected static class GatewayActuatorConfiguration {

        @Bean
        @ConditionalOnEnabledEndpoint
        public GatewayControllerEndpoint gatewayControllerEndpoint(
                RouteDefinitionLocator routeDefinitionLocator,
                List<GlobalFilter> globalFilters,
                List<GatewayFilterFactory> GatewayFilters,
                RouteDefinitionWriter routeDefinitionWriter,
                RouteLocator routeLocator) {
            return new GatewayControllerEndpoint(
                    routeDefinitionLocator,
                    globalFilters,
                    GatewayFilters,
                    routeDefinitionWriter,
                    routeLocator);
        }
    }

}