package cn.mauth.safe.gateway.filter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class LoginFilter implements GlobalFilter,Ordered{

    public LoginFilter() {
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate().request(exchange.getRequest()).build());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}