package com.kmpc.web.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

//    @RequestMapping({"", "/"})
//    public String index(){
//        return "pages/test/test";
//    }

    @RequestMapping({"2"})
    public String index2(){
        return "pages/board/list";
    }

    @RequestMapping({"1"})
    public String index1(){
        return "pages/MT/list";
    }
}
