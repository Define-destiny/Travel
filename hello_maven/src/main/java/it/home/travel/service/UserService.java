package it.home.travel.service;

import it.home.travel.domain.User;

/**
 * 用户信息操作接口
 */
public interface UserService {
    boolean registerUser(User user);
    boolean activeUser(String code);
    User loginUser(String username,String password);
}
