package it.home.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.domain.PageBean;
import it.home.travel.domain.Route;
import it.home.travel.service.RouteService;
import it.home.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 加载页面路线
 */
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    /**
     * 国内游分类页面路线加载
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void domesticTourism(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        int cid = 0;//类别id
        //2.处理参数
        if(cidStr != null && cidStr.length() > 0){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 5;
        }

        //3. 调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize);

        //4. 将pageBean对象序列化为json，返回
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(pb);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
