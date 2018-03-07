package ssm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: 郭航飞
 * @Description: 主页跳转测试
 * @Date: created in      2018/3/710:56
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/index",method = { RequestMethod.GET, RequestMethod.POST })
    public String main() {
        return "index";
    }
}
