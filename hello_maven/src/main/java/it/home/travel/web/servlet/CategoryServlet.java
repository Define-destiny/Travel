package it.home.travel.web.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.domain.Category;
import it.home.travel.service.CategoryService;
import it.home.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 初始化页面中的分类数据category
 */
public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryServiceImpl();
    public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //在缓存中获取category表的数据
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(categoryService.init());
    }
}
