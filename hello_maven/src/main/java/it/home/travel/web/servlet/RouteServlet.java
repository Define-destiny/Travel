package it.home.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.home.travel.domain.Favorite;
import it.home.travel.domain.PageBean;
import it.home.travel.domain.Route;
import it.home.travel.domain.User;
import it.home.travel.service.RouteService;
import it.home.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载页面路线
 */
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    ObjectMapper mapper = new ObjectMapper();
    /**
     * 分类页面路线加载
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void routeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.接受参数
        request.setCharacterEncoding("utf-8");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String nameStr = request.getParameter("rname");
        int cid = 0;//类别id
        //2.处理参数
        if(cidStr != null && cidStr.length() > 0 && !cidStr.equals("null")){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr)>0?Integer.parseInt(currentPageStr):1;
        }else{
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize = 5;
        }
        String name = null;
        if (nameStr != null && nameStr.length()>0 && !nameStr.equals("null")){
            nameStr = new String(nameStr.getBytes("iso-8859-1"),"utf-8");
            name = nameStr;
        }else{
            name = "";
        }
        //3. 调用service查询PageBean对象
        // System.out.println("cid="+cid+"\tcurrentPage="+currentPage+"\tpageSize="+pageSize+"\tname="+name);
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, name);
        //4. 将pageBean对象序列化为json，返回
        String json = mapper.writeValueAsString(pb);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 路线详情展示
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void routeDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        //获取路线rid
        String rid = request.getParameter("rid");
        //通过rid获取路线详细信息
        Route route = routeService.datailQuery(Integer.parseInt(rid));
        //封装成json并返回
        String json = mapper.writeValueAsString(route);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 判断登录用户是否收藏当前路线
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        //获取路线rid
        String rid = request.getParameter("rid");
        //获取登录用户信息
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        Map info = new HashMap();
        if (loginUser != null){
            info.put("login",true);
            //是否已经收藏
            if (routeService.isFavorite(Integer.parseInt(rid),loginUser.getUid())){
                //是
                info.put("flag",true);
            }
            else {
                //否
                info.put("flag",false);
            }
        }else {
            info.put("login",false);
        }
        response.setContentType("application/json;charset=utf-8");
        String s = mapper.writeValueAsString(info);
        response.getWriter().write(s);
    }
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        //获取路线rid
        String rid = request.getParameter("rid");
        //获取登录用户信息
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        //获取收藏时间
        String date = request.getParameter("date");

        Favorite favorite = new Favorite(Integer.parseInt(rid),date,loginUser.getUid());
        routeService.addFavorite(favorite);
        Map info = new HashMap();
        info.put("flag",true);
        response.setContentType("application/json;charset=utf-8");
        String s = mapper.writeValueAsString(info);
        response.getWriter().write(s);
    }
    public void collectList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取数据
        request.setCharacterEncoding("utf-8");
        String rnameStr = request.getParameter("rname");
        String priceDownStr = request.getParameter("priceDown");
        String priceUpStr = request.getParameter("priceUp");
        String currentPageStr = request.getParameter("currentPage");
        String rname = null;
        if (rnameStr!=null && rnameStr.length()>0 && !rnameStr.equals("undefined") ){
            rname = new String(rnameStr.getBytes("iso-8859-1"),"utf-8");
        }else rname = "";
        int priceDown = 0;
        if (priceDownStr!=null && priceDownStr.length()>0 && !priceDownStr.equals("undefined") ){
            priceDown = Integer.parseInt(priceDownStr);
        }
        int priceUp = 0;
        if (priceUpStr!=null && priceUpStr.length()>0 && !priceUpStr.equals("undefined")){
            priceUp = Integer.parseInt(priceUpStr);
        }
        int currentPage = 1;
        if (currentPageStr!=null && currentPageStr.length()>0 && !currentPageStr.equals("undefined")){
            currentPage = Integer.parseInt(currentPageStr);
        }
        //System.out.println("currentPage="+currentPage+"\tpriceDown="+priceDown+"\tpriceUp="+priceUp+"\trname="+rname);
        PageBean<Route> pageBean = routeService.pageCollectList(currentPage,priceDown,priceUp,rname);
        String s = mapper.writeValueAsString(pageBean);
        //System.out.println(s);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(s);
    }

}
