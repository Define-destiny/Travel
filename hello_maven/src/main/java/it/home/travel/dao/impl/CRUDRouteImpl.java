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


}


