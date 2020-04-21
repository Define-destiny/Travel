package it.home.travel.web.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.domain.User;
import it.home.travel.service.UserService;
import it.home.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
/**
 * 对用户进行管理
 */
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImpl();
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 用户注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String checkMsgJson = mapper.writeValueAsString(checkMap);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(checkMsgJson);
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String checkMsgJson = mapper.writeValueAsString(checkMap);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(checkMsgJson);
    }

    /**
     * 用户激活
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        request.setCharacterEncoding("utf-8");
        String code = request.getParameter("code");
        //激活
        UserService userService = new UserServiceImpl();
        userService.activeUser(code);
    }

    /**
     * 用户退出
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //删除登录用户session
        request.getSession().removeAttribute("loginUser");
        //跳转到主页面
        response.sendRedirect("/index.html");
    }

    /**
     * 登录用户信息封装
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void loginResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取登录用户
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        //封装进map
        String json = mapper.writeValueAsString(loginUser);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
