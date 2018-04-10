package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: 郭航飞
 * @Description: basePath路径获得
 * @Date: created in      2018/4/216:48
 */
public class BaseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain fic) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String prjPath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath();
        request.setAttribute("basePath", prjPath);
        fic.doFilter(req, rep);
    }

    @Override
    public void destroy() {

    }
}
