package me.s4h.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by LENOVO on 2014/11/21.
 */
@Controller
@RequestMapping("/shit")
public class ShitController {
    @RequestMapping("/")
    String index(){
        return "shit";
    }
}
