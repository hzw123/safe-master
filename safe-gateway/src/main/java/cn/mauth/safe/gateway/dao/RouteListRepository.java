package cn.mauth.safe.gateway.dao;

import cn.mauth.safe.gateway.domain.RouteList;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RouteListRepository extends BaseRepository<RouteList,Long>{

    @Query(value = "select server_id from route_list where id=:id",nativeQuery = true)
    String findServerIdById(@Param("id") Long id);

    @Query(value = "delete from route_list where server_id=:serverId",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByServerId(@Param("serverId") String serverId);

    @Query(value = "select *from route_list where server_id=:serverId",nativeQuery = true)
    Optional<RouteList> findByServerId(@Param("serverId") String serverId);
}
