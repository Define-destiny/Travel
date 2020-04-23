package it.home.travel.service;

import it.home.travel.domain.Favorite;
import it.home.travel.domain.PageBean;
import it.home.travel.domain.Route;

/**
 * 路线操作接口
 */
public interface RouteService {
    /**
     * 分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param name
     * @return
     */
    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String name);
    Route datailQuery(int rid);
    Boolean isFavorite(int rid,int uid);
    Boolean addFavorite(Favorite favorite);
    PageBean<Route> pageCollectList(int currentPage, int priceDown,int priceUp,String name);
}
