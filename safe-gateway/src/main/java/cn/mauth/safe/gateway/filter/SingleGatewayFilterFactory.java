package cn.mauth.safe.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashSet;

public class SingleGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    private static final String COUNT_START_TIME = "countStartTime";
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            exchange.getAttributes().put(COUNT_START_TIME, System.currentTimeMillis());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(()->{
                        Long startTime = exchange.getAttribute(COUNT_START_TIME);
                        Long endTime = System.currentTimeMillis() - startTime;
                        LinkedHashSet<URI> setUri=exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
                        String url="";
                        if(setUri.size()>0){
                            for (URI uri: setUri) {
                                url=uri.toString();
                                break;
                            }
                        }
                        if(startTime != null){
                            logger.info("\n\t"+url + ": "
                                    + endTime + "ms");
                        }
                    }));
        };
    }
}
