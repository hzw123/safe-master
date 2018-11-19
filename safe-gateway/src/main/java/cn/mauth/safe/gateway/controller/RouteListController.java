package cn.mauth.safe.gateway.controller;

import cn.mauth.safe.gateway.annotation.Api;
import cn.mauth.safe.gateway.dao.RouteListRepository;
import cn.mauth.safe.gateway.domain.RouteList;
import cn.mauth.safe.gateway.server.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/routes")
public class RouteListController {

    @Autowired
    private RouteListRepository repository;
    @Autowired
    private GatewayService service;

    @PostConstruct
    public void loadRoutes(){
        List<RouteList> routes=repository.findAll();
        if(routes.size()>0){
            for (RouteList r:routes) {
                service.save(r);
            }
        }
    }

    @GetMapping
    @Api("所有路由列表")
    public List<RouteList>  findAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RouteList> findById(@PathVariable Long id){
        return repository.findById(id);
    }

    @GetMapping("/server/{serverId}")
    public Optional<RouteList> findByServerId(@PathVariable String serverId){
        return repository.findByServerId(serverId);
    }

    @PostMapping("/save")
    public RouteList save(@RequestBody RouteList routeList){
        routeList.setCreateTime(new Date());
        routeList.setUpTime(new Date());
        service.save(repository.save(routeList));
        return routeList;
    }

    @PutMapping("/{id}")
    public RouteList updateById(@PathVariable Long id,@RequestBody RouteList routeList){
        if(id!=routeList.getId())
            return null;
        routeList.setUpTime(new Date());
        service.update(repository.save(routeList));
        return routeList;
    }

    @PutMapping("/server/{serverId}")
    public RouteList updateByServerId(@PathVariable String serverId,@RequestBody RouteList routeList){
        if(!serverId.equals(routeList.getServerId()))
            return null;
        service.save(repository.save(routeList));
        return routeList;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        service.delete(repository.findServerIdById(id));
        repository.deleteById(id);
    }

    @DeleteMapping("/server/{serverId}")
    public void deleteByServerId(@PathVariable String serverId){
        service.delete(serverId);
        repository.deleteByServerId(serverId);
    }
}
