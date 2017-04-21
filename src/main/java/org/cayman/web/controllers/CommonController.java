package org.cayman.web.controllers;


import org.cayman.dto.Author;
import org.cayman.dto.BookDto;
import org.cayman.dto.Publisher;
import org.cayman.service.AuthorService;
import org.cayman.service.BookService;
import org.cayman.service.CategoryService;
import org.cayman.service.PublisherService;
import org.cayman.utils.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CommonController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;

    @Autowired
    public CommonController(BookService bookService,
                            AuthorService authorService,
                            PublisherService publisherService,
                            CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "books", method = RequestMethod.GET)
    public String tables(Model model) {
        model.addAttribute("adminName", LoggedUser.getName());
        model.addAttribute("bookList", bookService.getAllBooksDto());
        model.addAttribute("authorsList", authorService.getAllAuthors());
        model.addAttribute("publishersList", publisherService.getAllPublishers());
        model.addAttribute("categoriesList", categoryService.getAllCategories());
        return "books";
    }

    @RequestMapping(value = "addBook", method = RequestMethod.GET)
    public String addBook(Model model) {
        List<String> authorsNames = authorService.getAllAuthors().stream().map(Author::getName).collect(Collectors.toList());
        List<String> publishersNames = publisherService.getAllPublishers().stream().map(Publisher::getName).collect(Collectors.toList());

        model.addAttribute("adminName", LoggedUser.getName());
        model.addAttribute("authorsName", authorsNames);
        model.addAttribute("publishersNames", publishersNames);
        model.addAttribute("categoriesList", categoryService.getAllCategories());
        return "addBook";
    }

    @RequestMapping(value = "editBook", method = RequestMethod.GET)
    public String editBook(Model model, @RequestParam("id") int id) {
        List<String> authorsNames = authorService.getAllAuthors().stream().map(Author::getName).collect(Collectors.toList());
        List<String> publishersNames = publisherService.getAllPublishers().stream().map(Publisher::getName).collect(Collectors.toList());
        BookDto editBook = bookService.getBookDtoById(id);

        model.addAttribute("adminName", LoggedUser.getName());
        model.addAttribute("authorsName", authorsNames);
        model.addAttribute("publishersNames", publishersNames);
        model.addAttribute("categoriesList", categoryService.getAllCategories());
        model.addAttribute("editBook", editBook);
        return "editBook";
    }


}
