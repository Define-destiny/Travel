package it.home.travel.service.impl;

import it.home.travel.dao.CRUDFavorite;
import it.home.travel.dao.CRUDRoute;
import it.home.travel.dao.CRUDUser;
import it.home.travel.dao.impl.CRUDFavoriteImpl;
import it.home.travel.dao.impl.CRUDRouteImpl;
import it.home.travel.dao.impl.CRUDUserImpl;
import it.home.travel.domain.PageBean;
import it.home.travel.domain.Route;
import it.home.travel.domain.User;
import it.home.travel.service.UserService;
import it.home.travel.utils.MailUtils;
import it.home.travel.utils.UuidUtil;

import java.util.List;

public class UserServiceImpl implements UserService {
    private CRUDUser crudUser = new CRUDUserImpl();
    private CRUDFavorite crudFavorite = new CRUDFavoriteImpl();
    private CRUDRoute crudRoute = new CRUDRouteImpl();
    @Override
    public boolean registerUser(User user) {
        if (crudUser.queryFromUsername(user.getUsername())!=null)
            return false;
        else{
            user.setStatus("N");
            user.setCode(UuidUtil.getUuid());
            crudUser.insertUser(user);
            MailUtils.sendMail(user.getEmail(),"<a href='http://localhost:8888/user/active"+"?code="+user.getCode()
                    +"'>点击激活</a>","激活邮件");
            return true;
        }
    }

    @Override
    public boolean activeUser(String code) {
        return crudUser.updateFormCode(code);
    }

    @Override
    public User loginUser(String username, String password) {
        return crudUser.queryFromUsnAPaw(username,password);
    }

    @Override
    public PageBean<Route> pageQuery(int uid, int currentPage, int pageSize) {
        PageBean<Route> pageBean = new PageBean<>();
        int totalCount = crudFavorite.findTotalCount(uid);
        pageBean.setTotalCount(totalCount);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalPage(totalCount%12==0?totalCount/12:totalCount/12+1);
        pageBean.setCurrentPage(currentPage);
        List<Route> byPage = crudRoute.findByPage(uid, (currentPage - 1) * pageSize, pageSize);
        pageBean.setList(byPage);
        return pageBean;
    }


}
