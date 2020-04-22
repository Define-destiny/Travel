package it.home.travel.dao.impl;

import it.home.travel.dao.CRUDRouteImg;
import it.home.travel.domain.RouteImg;
import it.home.travel.utils.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CRUDRouteImgImpl implements CRUDRouteImg {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtil.ds);
    @Override
    public List<RouteImg> findByRid(int rid) {
        String sql = "select * from tab_route_img where rid=?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<RouteImg>(RouteImg.class),rid);
    }
}
