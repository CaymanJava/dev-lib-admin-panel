package org.cayman.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.cayman.dto.Book;
import org.cayman.dto.SaveBookRequest;
import org.cayman.exception.SaveEntityException;
import org.cayman.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "books/add", method = RequestMethod.POST)
    public String addBook(@RequestParam("name") String name,
                          @RequestParam("lang") String lang,
                          @RequestParam("year") int year,
                          @RequestParam("authors") String authors,
                          @RequestParam("publisher") String publiser,
                          @RequestParam("description")String description,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("page") int page,
                          @RequestParam("category") int category
                          ) {
        Book book = bookService.saveBook(name, lang, year, authors, publiser, description, file, page, category);
        if (book == null || book.getId() == 0) throw new SaveEntityException("Author hadn't been updated");
        return "redirect:/books";
    }

    @RequestMapping(value = "/books/delete", method = RequestMethod.GET)
    public String downloadBook(@RequestParam("fileId") String fileId,
                               @RequestParam("id") int id,
                               @RequestParam("image") String image) {
        if (!bookService.deleteBook(fileId, id, image)) log.warn("Something went wrong. Please, check if book has been deleted");
        return "redirect:/books";
    }

    @RequestMapping(value = "/books/replace/image", method = RequestMethod.POST)
    public String replaceImage(@RequestParam("file") MultipartFile file,
                               @RequestParam("bookId") int id,
                               @RequestParam("oldLink") String oldLink,
                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("id", id);
        bookService.updateImage(file, id, oldLink);
        return "redirect:/editBook";
    }

    @RequestMapping(value = "/books/edit", method = RequestMethod.POST)
    public String updateBook(@RequestParam("id") int id,
                             @RequestParam("name") String name,
                             @RequestParam("lang") String lang,
                             @RequestParam("year") int year,
                             @RequestParam("authors") String authors,
                             @RequestParam("publisher") String publisher,
                             @RequestParam("imageLink") String image,
                             @RequestParam("fileId") String link,
                             @RequestParam("description") String description,
                             @RequestParam("category") int categoryId,
                             @RequestParam("pageCount") int pageCount) {
        bookService.updateBook(SaveBookRequest
                .builder()
                .id(id)
                .name(name)
                .language(lang)
                .year(year)
                .authors(BookService.prepareAuthors(authors))
                .publisher(publisher)
                .image(image)
                .link(link)
                .description(description)
                .categoryId(categoryId)
                .pageCount(pageCount)
                .build()
        );
        return "redirect:/books";
    }
}
