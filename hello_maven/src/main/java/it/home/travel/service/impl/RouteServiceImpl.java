package it.home.travel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.dao.CRUDRoute;
import it.home.travel.dao.impl.CRUDRouteImpl;
import it.home.travel.domain.PageBean;
import it.home.travel.domain.Route;
import it.home.travel.service.RouteService;
import it.home.travel.utils.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private CRUDRoute crudRoute = new CRUDRouteImpl();
    private Jedis jedis = JedisUtil.getJedis();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = crudRoute.findTotalCount(cid);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Route> list = crudRoute.findByPage(cid,start,pageSize);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
        pb.setTotalPage(totalPage);


        return pb;
    }
}
