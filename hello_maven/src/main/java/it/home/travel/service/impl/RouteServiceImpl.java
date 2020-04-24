package it.home.travel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.dao.*;
import it.home.travel.dao.impl.*;
import it.home.travel.domain.*;
import it.home.travel.service.RouteService;
import it.home.travel.utils.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private CRUDRoute crudRoute = new CRUDRouteImpl();
    private CRUDCategory crudCategory = new CRUDCategoryImpl();
    private CRUDSeller crudSeller = new CRUDSellerImpl();
    private CRUDRouteImg crudRouteImg = new CRUDRouteImgImpl();
    private CRUDFavorite crudFavorite = new CRUDFavoriteImpl();
    private Jedis jedis = JedisUtil.getJedis();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String name) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = crudRoute.findTotalCount(cid,name);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Route> list = crudRoute.findByPage(cid,start,pageSize,name);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
        pb.setTotalPage(totalPage);


        return pb;
    }

    @Override
    public Route datailQuery(int rid) {
        //获得路线基本信息
        Route route = new Route();
        route = crudRoute.findByDetail(rid);
        //根据基本信息里的cid获取路线分类信息
        Category category = new Category();
        category = crudCategory.findByCid(route.getCid());
        //根据基本信息里的sid获取商家信息
        Seller seller = new Seller();
        seller = crudSeller.findBySid(route.getSid());
        //根据基本信息里的rid获取路线详细图片
        List<RouteImg> routeImgs= new ArrayList<>();
        routeImgs = crudRouteImg.findByRid(route.getRid());

        //设置路线详细信息
        route.setCategory(category);
        route.setSeller(seller);
        route.setRouteImgList(routeImgs);
        return route;
    }

    @Override
    public Boolean isFavorite(int rid, int uid) {
        Favorite favorite = crudFavorite.findByRidAUid(rid,uid);
        return favorite!=null?true:false;
    }

    @Override
    public Boolean addFavorite(Favorite favorite) {
        if (crudFavorite.insertOne(favorite)){
            crudRoute.updateCountAddOne(favorite.getRid());
            return true;
        }else return false;
    }

    @Override
    public PageBean<Route> pageCollectList(int currentPage, int priceDown, int priceUp, String name) {
        PageBean<Route> pageBean= new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        int totalCount = crudRoute.findTotalCount(priceDown, priceUp, name);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalCount%8==0?totalCount/8:totalCount/8+1);
        pageBean.setPageSize(8);
        pageBean.setList(crudRoute.findByPage(priceDown,priceUp,(currentPage-1)*8,8,name));
        return pageBean;
    }

    @Override
    public PageBean<Route> pageTimeChart(int currentPage, String name) {
        PageBean<Route> pageBean= new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        int totalCount = crudRoute.findTotalCount(0, name);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalCount%8==0?totalCount/8:totalCount/8+1);
        pageBean.setPageSize(8);
        pageBean.setList(crudRoute.findByPageTime(0,(currentPage-1)*8,8,name));
        return pageBean;
    }


}
