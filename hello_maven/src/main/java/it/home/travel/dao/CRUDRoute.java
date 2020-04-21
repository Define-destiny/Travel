package it.home.travel.dao;

import it.home.travel.domain.Route;

import java.util.List;

/**
 * 操作tab_route表以及相关表的接口
 */
public interface CRUDRoute {

    /**
     * 根据cid查询总记录数
     */
    public int findTotalCount(int cid);

    /**
     * 根据cid，start,pageSize查询当前页的数据集合
     */
    public List<Route> findByPage(int cid , int start , int pageSize);
}
