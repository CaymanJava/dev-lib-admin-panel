package org.cayman.service;

import lombok.extern.slf4j.Slf4j;
import org.cayman.dto.Author;
import org.cayman.dto.SaveDto;
import org.cayman.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthorService {
    private final Constants constants;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthorService(Constants constants, RestTemplate restTemplate) {
        this.constants = constants;
        this.restTemplate = restTemplate;
    }

    public List<Author> getAllAuthors() {
        String url = constants.getBookServiceUrl() + "/author";
        Author[] authors = restTemplate.getForObject(url, Author[].class);
        return Arrays.asList(authors);
    }

    public Author saveAuthor(String name) {
        String url = constants.getBookServiceUrl() + "/author/save";
        return restTemplate.postForObject(url, new SaveDto(name), Author.class);
    }

    public Author updateAuthor(int id, String name) {
        String url = constants.getBookServiceUrl() + "/author/update";
        return restTemplate.postForObject(url, new Author(id, name), Author.class);
    }

    public boolean deleteAuthor(int id) {
        String url = constants.getBookServiceUrl() + "/author/delete?id={id}";
        return restTemplate.getForObject(url, Boolean.class, id);
    }
}
