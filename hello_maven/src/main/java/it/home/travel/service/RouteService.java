package it.home.travel.service;

import it.home.travel.domain.PageBean;
import it.home.travel.domain.Route;

/**
 * 路线操作接口
 */
public interface RouteService {
    /**
     * 根据类别进行分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize);
}
