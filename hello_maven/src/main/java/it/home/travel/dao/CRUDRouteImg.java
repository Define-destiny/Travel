package it.home.travel.dao;

import it.home.travel.domain.RouteImg;

import java.util.List;

/**
 * 操作tab_route_img表
 */
public interface CRUDRouteImg {
    List<RouteImg> findByRid(int rid);
}
