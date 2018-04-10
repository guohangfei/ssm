package controller;

import bean.ResultBean;
import bean.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author: 郭航飞
 * @Description: 主页跳转测试
 * @Date: created in      2018/3/710:56
 */
@Controller
public class IndexController {

    private Logger log = LoggerFactory.getLogger("IndexController");
    @RequestMapping(value = "/index",method = { RequestMethod.GET, RequestMethod.POST })
    public String main() {
        log.info("进入首页:");
        return "index";
    }

    /**
     * 测试跨域的方案
     */
    @ResponseBody
    @RequestMapping(value = "/get1")
    public ResultBean get1(Date date){
        return  new ResultBean(date.toString());
    }

    /**
       *带json的ajax请求
     **/
    @ResponseBody
    @RequestMapping(value = "/postJson")
    public  ResultBean postJson(@RequestBody Users user){
        System.out.println("postJson 执行");
        return new ResultBean("postJson:"+user.getName());
    }

    /**
     *带cookies的ajax请求
     **/
    @ResponseBody
    @RequestMapping(value = "/attachCookie")
    public  ResultBean  attachCookie(@CookieValue(value = "cookie")String cookie){
        System.out.println("带cookie 的ajax跨域执行");
        return new ResultBean("浏览器cookie是：:"+cookie);
    }

    /**
     *带header的ajax请求
     **/
    @ResponseBody
    @RequestMapping(value = "/attachHeader")
    public  ResultBean  attachHeader(@RequestHeader(value = "x-header1")String header1,
                                     @RequestHeader(value = "x-header2") String header2){
        System.out.println("带header 的ajax跨域执行");
        return new ResultBean("浏览器header1=:"+header1+"hander2="+header2);
    }
}
