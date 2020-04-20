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
 * 注册
 */
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取验证码信息
        request.setCharacterEncoding("utf-8");
        String check = request.getParameter("check");
        //获取校验验证码
        HttpSession session = request.getSession();
        Map checkMap = new HashMap<>();

        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        //获取注册用户信息
        Map parameterMap = request.getParameterMap();
        //封装注册用户信息
        User registerUser = new User();
        try {
            BeanUtils.populate(registerUser,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //验证注册
        if (checkcode_server!=null && checkcode_server.toLowerCase().equals(check.toLowerCase())){
            UserService userService = new UserServiceImpl();
            if (userService.registerUser(registerUser)){
                checkMap.put("flag",true);
            }else {
                checkMap.put("flag",false);
                checkMap.put("msg","注册失败,用户名已存在");
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
