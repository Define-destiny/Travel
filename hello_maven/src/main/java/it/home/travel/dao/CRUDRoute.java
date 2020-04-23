package it.home.travel.dao;

import it.home.travel.domain.Route;

import java.util.List;

/**
 * 操作tab_route表以及相关表的接口
 */
public interface CRUDRoute {

    /**
     * 根据cid,是否包含name查询总记录数
     */
    int findTotalCount(int cid,String name);

    /**
     * 根据cid，start,pageSize,是否包含name查询当前页的数据集合
     */
    List<Route> findByPage(int cid , int start , int pageSize,String name);

    /**
     * 根据rid查询当前路线信息
     */
    Route findByDetail(int rid);

    boolean updateCountAddOne(int rid);

    List<Route> findByPage(int uid,int start, int pageSize);

    int findTotalCount(int priceDown,int priceUp,String name);

    List<Route> findByPage(int priceDown,int priceUp,int start, int pageSize,String name);
}
