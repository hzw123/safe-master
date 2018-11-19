package cn.mauth.safe.gateway.server;

import cn.mauth.safe.gateway.domain.RouteList;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class GatewayService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher=applicationEventPublisher;
    }

    public boolean save(RouteList routeList) {
        if(routeList==null)
            return false;
        RouteDefinition definition = new RouteDefinition();
        PredicateDefinition predicate = new PredicateDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);

        if(StringUtils.isNotEmpty(routeList.getServerId())){
            definition.setId(routeList.getServerId());
        }
        definition.setOrder(routeList.getLocal());
        URI uri = UriComponentsBuilder.fromHttpUrl(routeList.getUrl()).build().toUri();
        definition.setUri(uri);

        predicate.setName("Path");
        predicateParams.put("pattern", routeList.getPath());
        predicateParams.put("pathPattern", routeList.getPath());
        predicate.setArgs(predicateParams);
        definition.setPredicates(Arrays.asList(predicate));
        definition.setFilters(
                Arrays.asList(
                        rate(routeList.getReplenishRate(), routeList.getBurstCapacity()),
                        stripPrefix(routeList.getPrefix())
                )
        );
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();

        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return true;
    }

    private FilterDefinition rate(int replenishRate,int burstCapacity){
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        // 名称是固定的，spring gateway会根据名称找对应的FilterFactory
        filterDefinition.setName("RequestRateLimiter");
        // 每秒最大访问次数
        filterParams.put("redis-rate-limiter.replenishRate", String.valueOf(replenishRate));
        // 令牌桶最大容量
        filterParams.put("redis-rate-limiter.burstCapacity", String.valueOf(burstCapacity));
        // 限流策略(#{@BeanName})
        filterParams.put("key-resolver", "#{@ipKeyResolver}");
        filterDefinition.setArgs(filterParams);
        return filterDefinition;
    }

    private FilterDefinition stripPrefix(int prefix){
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        filterDefinition.setName("StripPrefix");
        filterParams.put("parts",String.valueOf(prefix));
        filterDefinition.setArgs(filterParams);
        return filterDefinition;
    }

    public boolean update(RouteList routeList){
        if(delete(routeList.getServerId())){
            if(save(routeList)){
                return true;
            }
        }

        return false;
    }

    public boolean delete(String serverId){
        routeDefinitionWriter.delete(Mono.just(serverId));
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return true;
    }
}
