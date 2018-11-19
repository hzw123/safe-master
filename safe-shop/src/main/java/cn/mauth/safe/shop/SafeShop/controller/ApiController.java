package cn.mauth.safe.shop.SafeShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/shops")
public class ApiController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/shop")
    @ResponseBody
    public String shop(){
        return "shop page";
    }
}
