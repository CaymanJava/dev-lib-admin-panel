package org.cayman.web.controllers;

import org.cayman.dto.Author;
import org.cayman.exception.DeleteEntityException;
import org.cayman.exception.SaveEntityException;
import org.cayman.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping(value = "author/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addAuthor(@RequestParam("name") String name) {
        Author author = authorService.saveAuthor(name);
        if (author == null) throw new SaveEntityException("Author hadn't been saved");
        return "redirect:/books";
    }

    @RequestMapping(value = "author/edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editAuthor(@RequestParam("id") int id, @RequestParam("name") String name) {
        Author author = authorService.updateAuthor(id, name);
        if (author == null) throw new SaveEntityException("Author hadn't been updated");
        return "redirect:/books";
    }

    @RequestMapping(value = "author/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAuthor(@RequestParam("id") int id) {
        boolean response = authorService.deleteAuthor(id);
        if (!response) throw new DeleteEntityException("Author hadn't been deleted");
        return "redirect:/books";
    }
}
