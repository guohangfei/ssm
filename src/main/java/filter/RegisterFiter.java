package filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: 郭航飞
 * @Description: 跨域请求过滤器
 * @Date: created in      2018/4/316:56
 */
public class RegisterFiter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse res=(HttpServletResponse)servletResponse;
        HttpServletRequest req=(HttpServletRequest)servletRequest;

        String origin = req.getHeader("Origin");
        if (!StringUtils.isEmpty(origin)){
            res.addHeader("Access-Control-Allow-Origin",origin);
        }
        String headers = req.getHeader("Access-Control-Request-Headers");
        if (!StringUtils.isEmpty(headers)){
            res.addHeader("Access-Control-Allow-Headers",headers);
        }
        res.addHeader("Access-Control-Allow-Methods","*");
//      带cookie时候需满足则条件
        res.addHeader("Access-Control-Allow-Credentials","true");
        //     用于缓存option请求，减少浏览器的判断
        res.addHeader("Access-Control-Max-Age","3600");

        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {

    }
}
