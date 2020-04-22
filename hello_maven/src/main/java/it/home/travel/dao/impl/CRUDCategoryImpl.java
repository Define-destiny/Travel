package it.home.travel.dao.impl;

import it.home.travel.dao.CRUDCategory;
import it.home.travel.domain.Category;
import it.home.travel.utils.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class CRUDCategoryImpl implements CRUDCategory {
    private JdbcTemplate jtl = new JdbcTemplate(JDBCUtil.ds);
    @Override
    public List<Category> findAll() {
        String sql = "select * from tab_category";
        List<Category> list = new ArrayList<>();
        try {
            list = jtl.query(sql,new BeanPropertyRowMapper<Category>(Category.class));
        }catch (Exception e){
            System.out.println("category error");
        }
        return list;
    }

    @Override
    public Category findByCid(int cid) {
        String sql = "select * from tab_category where cid=?";
        return jtl.queryForObject(sql,new BeanPropertyRowMapper<Category>(Category.class),cid);
    }

}
