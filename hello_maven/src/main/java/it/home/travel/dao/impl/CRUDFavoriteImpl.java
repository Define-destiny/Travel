package it.home.travel.dao.impl;

import it.home.travel.dao.CRUDFavorite;
import it.home.travel.domain.Favorite;
import it.home.travel.utils.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;

public class CRUDFavoriteImpl implements CRUDFavorite {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtil.ds);
    @Override
    public Favorite findByRidAUid(int rid, int uid) {
        Favorite favorite = null;
        String sql = "select * from tab_favorite where rid=? and uid=?";
        try {
            favorite = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        }catch (Exception e){
            System.out.println("用户未收藏");
        }
        return favorite;
    }

    @Override
    public boolean insertOne(Favorite favorite) {
        String sql = "insert into tab_favorite(rid,date,uid) values(?,?,?)";
        return jdbcTemplate.update(sql,favorite.getRid(),new Date(Long.parseLong(favorite.getDate())).toString(),favorite.getUid())>0;
    }

    @Override
    public int findTotalCount(int uid) {
        String sql = "select count(*) from tab_favorite where uid = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, uid);
    }
}
