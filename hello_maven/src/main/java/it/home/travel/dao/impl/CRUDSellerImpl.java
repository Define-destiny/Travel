package it.home.travel.dao.impl;

import it.home.travel.dao.CRUDSeller;
import it.home.travel.domain.Seller;
import it.home.travel.utils.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class CRUDSellerImpl implements CRUDSeller {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtil.ds);
    @Override
    public Seller findBySid(int sid) {
        String sql = "select * from tab_seller where sid=?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Seller>(Seller.class),sid);
    }
}
