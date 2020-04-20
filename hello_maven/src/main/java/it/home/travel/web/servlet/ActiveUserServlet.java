package it.home.travel.web.servlet;

import it.home.travel.service.UserService;
import it.home.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 激活用户
 */
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        request.setCharacterEncoding("utf-8");
        String code = request.getParameter("code");
        //激活
        UserService userService = new UserServiceImpl();
        userService.activeUser(code);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
