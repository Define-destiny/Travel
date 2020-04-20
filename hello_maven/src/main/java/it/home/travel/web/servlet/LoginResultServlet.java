package it.home.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.domain.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * 处理登录后用户信息
 */
public class LoginResultServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取登录用户
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        //封装进map
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(loginUser);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
