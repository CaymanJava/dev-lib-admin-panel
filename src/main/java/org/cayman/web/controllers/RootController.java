package org.cayman.web.controllers;

import org.cayman.utils.LoggedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/")
public class RootController {
    @RequestMapping(method = RequestMethod.GET)
    public String root(){
        return "redirect:books";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model,
                        @RequestParam(value = "error", required = false) boolean error,
                        @RequestParam(value = "message", required = false) String message) {
        model.put("error", error);
        model.put("message", message);
        return "login";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied(Model model){
        model.addAttribute("adminName", LoggedUser.getName());
        return "accessDenied";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model){
        model.addAttribute("adminName", LoggedUser.getName());
        return "error";
    }
}
