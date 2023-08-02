package com.kmpc.web.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

//    @RequestMapping({"", "/"})
//    public String index(){
//        return "pages/test/test";
//    }

    @RequestMapping({"2"})
    public String index2(HttpServletRequest req){

        return "pages/board/list";
    }

    @RequestMapping({"1"})
    public String index1(){
        return "mtList";
    }
}
