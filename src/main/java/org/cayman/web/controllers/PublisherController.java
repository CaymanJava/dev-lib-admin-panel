package org.cayman.web.controllers;

import org.cayman.dto.Publisher;
import org.cayman.exception.DeleteEntityException;
import org.cayman.exception.SaveEntityException;
import org.cayman.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @RequestMapping(value = "publisher/add", method = RequestMethod.POST)
    public String addPublisher(@RequestParam("name") String name) {
        Publisher publisher = publisherService.savePublisher(name);
        if (publisher == null) throw new SaveEntityException("Publisher hadn't been saved");
        return "redirect:/books";
    }

    @RequestMapping(value = "publisher/edit", method = RequestMethod.POST)
    public String editPublisher(@RequestParam("id") int id, @RequestParam("name") String name) {
        Publisher publisher = publisherService.updatePublisher(id, name);
        if (publisher == null) throw new SaveEntityException("Publisher hadn't been updated");
        return "redirect:/books";
    }

    @RequestMapping(value = "publisher/delete", method = RequestMethod.POST)
    public String deletePublisher(@RequestParam("id") int id) {
        boolean response = publisherService.deletePublisher(id);
        if (!response) throw new DeleteEntityException("Publisher hadn't been deleted");
        return "redirect:/books";
    }
}
