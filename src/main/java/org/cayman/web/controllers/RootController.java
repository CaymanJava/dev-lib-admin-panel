package org.cayman.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class RootController {
    @RequestMapping(method = RequestMethod.GET)
    public String root(){
        return "index";
    }
}
