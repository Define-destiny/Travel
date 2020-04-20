package it.home.travel.service.impl;

import it.home.travel.dao.CRUDUser;
import it.home.travel.dao.impl.CRUDUserImpl;
import it.home.travel.domain.User;
import it.home.travel.service.UserService;
import it.home.travel.utils.MailUtils;
import it.home.travel.utils.UuidUtil;

public class UserServiceImpl implements UserService {
    private CRUDUser crudUser = new CRUDUserImpl();
    @Override
    public boolean registerUser(User user) {
        if (crudUser.queryFromUsername(user.getUsername())!=null)
            return false;
        else{
            user.setStatus("N");
            user.setCode(UuidUtil.getUuid());
            crudUser.insertUser(user);
            MailUtils.sendMail(user.getEmail(),"<a href='http://localhost:8888/activeUserServlet"+"?code="+user.getCode()
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


}
