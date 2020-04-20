package it.home.travel.dao;

import it.home.travel.domain.User;
/**
 * 操作tab_user表的接口
 */
public interface CRUDUser {
    User queryFromUsername(String username);
    void insertUser(User user);
    boolean updateFormCode(String code);
    User queryFromUsnAPaw(String username,String password);
}
