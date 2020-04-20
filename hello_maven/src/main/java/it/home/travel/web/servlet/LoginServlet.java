package it.home.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.domain.User;
import it.home.travel.service.UserService;
import it.home.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
/**
 * 登录
 */
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取验证码信息
        request.setCharacterEncoding("utf-8");
        String check = request.getParameter("check");
        //获取校验验证码
        HttpSession session = request.getSession();
        Map checkMap = new HashMap<>();

        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        //获取登录用户信息
        Map parameterMap = request.getParameterMap();
        //封装登录用户信息
        User loginUser = new User();
        try {
            BeanUtils.populate(loginUser,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (checkcode_server!=null && checkcode_server.toLowerCase().equals(check.toLowerCase())){
            UserService userService = new UserServiceImpl();
            loginUser = userService.loginUser(loginUser.getUsername(),loginUser.getPassword());
            if(loginUser!=null){
                //账号已经激活
                if (loginUser.getStatus().equals("Y")){
                    checkMap.put("flag",true);
                    //如果登录成功则将用户信息存储在session中
                    session.setAttribute("loginUser",loginUser);
                }else {
                    checkMap.put("flag",false);
                    checkMap.put("msg","账号未激活");
                }
            }else {
                checkMap.put("flag",false);
                checkMap.put("msg","用户名或密码错误");
            }
        }else {
            checkMap.put("flag",false);
            checkMap.put("msg","验证码输入错误");
        }
        ObjectMapper mapper = new ObjectMapper();
        String checkMsgJson = mapper.writeValueAsString(checkMap);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(checkMsgJson);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
