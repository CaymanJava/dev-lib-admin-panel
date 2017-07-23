package org.cayman.web.controllers;

import org.cayman.service.BookService;
import org.cayman.service.MessageService;
import org.cayman.service.UserService;
import org.cayman.utils.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

    private final BookService bookService;
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public DashboardController(BookService bookService, UserService userService, MessageService messageService) {
        this.bookService = bookService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String dashboard(Model model) {
        model.addAttribute("adminName", LoggedUser.getName());
        model.addAttribute("bookCount", bookService.getBooksCount());
        model.addAttribute("newMessageCount", messageService.getNewMessageCount());
        model.addAttribute("userCount", userService.getUserCount());
        model.addAttribute("messageList", messageService.getAllMessages());
        return "dashboard";
    }
}
