package cn.mauth.safe.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashSet;

@Configuration
public class LogFilter implements GlobalFilter,Ordered{

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    public LogFilter() {
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LinkedHashSet<URI> setUri=exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        String url="";
        if(setUri.size()>0){
            int i=0;
            for (URI uri: setUri) {
                if(i!=0){
                    url+="\n\t url-2"+uri.toString();
                }
                url+=uri.toString();
                i++;
            }
        }
        Route route=exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI routeUri=exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        logger.info("\n\n=====================================================" +
                "\n\turl:"+url+
                "\n\t路由："+route.getId()+
                "\n\t转发到："+routeUri.toString()+
                "\n===============================================================\n");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 20000;
    }
}
