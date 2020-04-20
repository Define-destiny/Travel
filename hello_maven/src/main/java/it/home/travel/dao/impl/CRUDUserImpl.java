package it.home.travel.dao.impl;

import it.home.travel.dao.CRUDUser;
import it.home.travel.domain.User;
import it.home.travel.utils.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class CRUDUserImpl implements CRUDUser {
    private JdbcTemplate dtl = new JdbcTemplate(JDBCUtil.ds);
    @Override
    public User queryFromUsername(String username) {
        String sql = "select * from tab_user where username='"+username+"'";
        User user=null;
        try{
            user = dtl.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class));
        }catch (Exception e){
            System.out.println("username:"+username+"\tcan register");
        }
        return user;
    }

    @Override
    public void insertUser(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) " +
                "values(?,?,?,?,?,?,?,?,?)";
        dtl.update(sql,user.getUsername(),user.getPassword(),user.getName(),user.getBirthday(),user.getSex(),user.getTelephone(),
                user.getEmail(),user.getStatus(),user.getCode());
    }

    @Override
    public boolean updateFormCode(String code) {
        String sql = "update tab_user set status='Y' where code='"+code+"'";
        return dtl.update(sql)>0;
    }

    @Override
    public User queryFromUsnAPaw(String username, String password) {
        String sql = "select * from tab_user where username='"+username+"' and password = '"+password+"'";
        User user=null;
        try{
            user = dtl.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class));
        }catch (Exception e){
            System.out.println("username:"+username+"\tcannot find");
        }
        return user;
    }
}
