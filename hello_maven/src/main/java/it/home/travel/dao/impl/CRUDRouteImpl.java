package it.home.travel.dao.impl;

import it.home.travel.dao.CRUDRoute;
import it.home.travel.domain.Route;
import it.home.travel.utils.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;


public class CRUDRouteImpl implements CRUDRoute {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtil.ds);

    @Override
    public int findTotalCount(int cid, String name) {
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List list = new ArrayList();
        if (cid != 0) {
            sb.append("and cid = ? ");
            list.add(cid);
        }
        if (name != null || name.length() > 0) {
            sb.append("and rname like ?");
            list.add("%" + name + "%");
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, list.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String name) {
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List list = new ArrayList();
        if (cid != 0) {
            sb.append("and cid = ? ");
            list.add(cid);
        }
        if (name != null || name.length() > 0) {
            sb.append("and rname like ? ");
            list.add("%" + name + "%");
        }
        sb.append("limit ?,?");
        list.add(start);
        list.add(pageSize);
        sql = sb.toString();
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), list.toArray());
    }

    @Override
    public Route findByDetail(int rid) {
        String sql = "select * from tab_route where rid=?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }

    @Override
    public boolean updateCountAddOne(int rid) {
        String sql = "update tab_route set count = count+1 where rid=?";
        return template.update(sql,rid)>0;
    }

    @Override
    public List<Route> findByPage(int uid, int start, int pageSize) {
        String sql = "select * from tab_favorite f join tab_route r on f.rid=r.rid having f.uid = ? limit ?,?";
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),uid,start,pageSize);
    }

    @Override
    public int findTotalCount(int priceDown, int priceUp, String name) {
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List list = new ArrayList();
        if (priceUp>priceDown){
            sb.append("and price between ? and ? ");
            list.add(priceDown);
            list.add(priceUp);
        }
        if (name!=null&&name.length()>0){
            sb.append("and rname like ?");
            list.add("%"+name+"%");
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, list.toArray());
    }

    @Override
    public List<Route> findByPage(int priceDown, int priceUp, int start, int pageSize, String name) {
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List list = new ArrayList();
        if (priceUp>priceDown){
            sb.append("and price between ? and ? ");
            list.add(priceDown);
            list.add(priceUp);
        }
        if (name!=null&&name.length()>0){
            sb.append("and rname like ?");
            list.add("%"+name+"%");
        }
        sb.append(" order by count desc limit ?,?");
        list.add(start);
        list.add(pageSize);
        sql = sb.toString();
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),list.toArray());
    }

    @Override
    public List<Route> findByPageTime(int cid, int start, int pageSize ,String name) {
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        List list = new ArrayList();
        if (cid != 0) {
            sb.append("and cid = ? ");
            list.add(cid);
        }
        if (name!=null&&name.length()>0){
            sb.append("and rname like ?");
            list.add("%"+name+"%");
        }
        sb.append(" order by rdate desc limit ?,?");
        list.add(start);
        list.add(pageSize);
        sql = sb.toString();
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),list.toArray());
    }
}


